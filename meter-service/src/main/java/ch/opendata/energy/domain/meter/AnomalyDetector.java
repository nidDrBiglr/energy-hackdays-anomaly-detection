package ch.opendata.energy.domain.meter;

import ch.opendata.energy.config.ReactiveKafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AnomalyDetector {
  private final ReactiveKafkaConsumerTemplate<String, MeterUplinkEvent> receiver;
  private final Scheduler scheduler;
  private final AnomalyRepository anomalyRepository;

  public AnomalyDetector(ReactiveKafkaConfig config, AnomalyRepository anomalyRepository) {
    this.receiver = config.consumer("meter_data", "outputs.anomalies", true);
    this.anomalyRepository = anomalyRepository;
    this.scheduler = Schedulers.newElastic("anomalyProcessor", 10, true);
    receiver
      .receive()
      .publishOn(scheduler)
      .concatMap(message -> consume(message.value())
        .thenEmpty(message.receiverOffset().commit())
        .retryWhen(Retry.backoff(10, Duration.ofMillis(500))))
      .doOnError(e -> log.error("could not process Mongo output", e))
      .doOnCancel(() -> close())
      .subscribe();
  }

  public void close() {
    scheduler.dispose();
  }

  public Flux consume(MeterUplinkEvent event) {
    //TODO keep track of the window for the prediction for timeseries forecast
    if (event.getTimestamp() == null) {
      return Flux.just("");
    }

    List<AnomalyDocument> anomalies = new ArrayList<>();
    if (event.getKWh() != null && event.getKWh() <= 0) {
      AnomalyDocument anomaly = AnomalyDocument.builder()
        .kWh(event.getKWh())
        .timestamp(event.getTimestamp())
        .meterId(event.getMeterId())
        .build();
      anomalies.add(anomaly);
    }

    return getClient()
      .post()
      .uri(builder -> builder.path("/predict").build())
      .body(BodyInserters.fromValue(event))
      .retrieve()
      .bodyToMono(PredictionResponse.class)
      .map(response -> {

        if (response.isSuccess()) {
          if (response.getLabel().equals("anomalous")) {
            log.info("anomaly detected");
            AnomalyDocument anomaly = AnomalyDocument.builder()
              .kWh(event.getKWh())
              .timestamp(event.getTimestamp())
              .meterId(event.getMeterId())
              .build();
            anomalies.add(anomaly);
          } else {
            log.info("no anomaly detected");
          }
        } else {
          log.error("anomaly detection unsuccessful");
        }
        return response;
      }).flatMapMany(response -> anomalyRepository.saveAll(anomalies));
  }

  private WebClient getClient() {
    return WebClient
      .builder()
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
      .baseUrl("http://anomaly-detector-internal")
      .filter(checkStatus())
      .build();
  }

  private ExchangeFilterFunction checkStatus() {
    return ExchangeFilterFunction.ofResponseProcessor(response -> {
      HttpStatus status = response.statusCode();
      if (!status.is2xxSuccessful()) {
        throw new ResponseStatusException(status, "could not get prediction");
      }

      return Mono.just(response);
    });
  }
}
