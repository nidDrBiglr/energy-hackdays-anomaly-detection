package ch.opendata.energy.domain.meter;

import ch.opendata.energy.config.ReactiveKafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class AnomalyDetector {
  private final ReactiveKafkaConsumerTemplate<String, MeterUplinkEvent> receiver;
  private final Scheduler scheduler;

  public AnomalyDetector(ReactiveKafkaConfig config) {
    this.receiver = config.consumer("meter_data", "outputs.anomalies", true);
    this.scheduler = Schedulers.newElastic("anomalyProcessor", 10, true);

    receiver
      .receive()
      .publishOn(scheduler)
      .concatMap(message -> consume(message.value())
        .thenEmpty(message.receiverOffset().commit())
        .log()
        .retryWhen(Retry.backoff(10, Duration.ofMillis(500))))
      .doOnError(e -> log.error("could not process Mongo output", e))
      .doOnCancel(() -> close())
      .subscribe();
  }

  public void close() {
    scheduler.dispose();
  }

  public Mono<String> consume(MeterUplinkEvent event) {
    //TODO implement anomaly detection

    //TODO keep track of the window for the prediction
    return Mono.empty();
  }
}
