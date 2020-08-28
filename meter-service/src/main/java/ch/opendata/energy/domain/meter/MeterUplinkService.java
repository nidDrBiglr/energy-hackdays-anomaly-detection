package ch.opendata.energy.domain.meter;

import ch.opendata.energy.config.ReactiveKafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.util.UUID;

@Service
@Slf4j
public class MeterUplinkService {
  private ReactiveKafkaProducerTemplate<String, MeterUplinkEvent> producer;

  public MeterUplinkService(ReactiveKafkaConfig config) {
    this.producer = config.producer("meter-uplink");
  }

  public Mono<SenderResult<Void>> sendMessage(MeterUplinkEvent message) {
    log.info(String.format("#### -> Producing message -> %s", message));
    return producer.send("meter_data", UUID.randomUUID().toString(), message);
  }
}
