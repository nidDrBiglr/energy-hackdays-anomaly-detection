package ch.opendata.energy.config;

import ch.opendata.energy.domain.meter.MeterUplinkEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@Configuration
@Slf4j
public class ReactiveKafkaConfig {
  private final KafkaProperties properties;
  private final ObjectMapper objectMapper;

  public ReactiveKafkaConfig(KafkaProperties properties, ObjectMapper objectMapper) {
    this.properties = properties;
    this.objectMapper = objectMapper;
  }

  public ReactiveKafkaProducerTemplate<String, MeterUplinkEvent> producer(String clientId) {
    Map<String, Object> props = properties.buildProducerProperties();
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    JsonSerializer<MeterUplinkEvent> serializer = new JsonSerializer<>(objectMapper);

    SenderOptions<String, MeterUplinkEvent> senderOptions = SenderOptions.<String, MeterUplinkEvent>create(props).withValueSerializer(serializer);
    return new ReactiveKafkaProducerTemplate<>(senderOptions);
  }

  public ReactiveKafkaConsumerTemplate<String, MeterUplinkEvent> consumer(String sourceTopic, String consumerGroup, boolean disablePeriodicCommits) {
    Map<String, Object> props = properties.buildConsumerProperties();

    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "meter-consumer");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    JsonDeserializer<MeterUplinkEvent> jsonDeserializer = new JsonDeserializer<>();
    jsonDeserializer.addTrustedPackages("*");

    ReceiverOptions options = ReceiverOptions.<String, MeterUplinkEvent>create(props)
      .addAssignListener(partitions -> log.info("onPartitionsAssigned {}", partitions))
      .addRevokeListener(partitions -> log.info("onPartitionsRevoked {}", partitions))
      .withValueDeserializer(jsonDeserializer)
      .subscription(Arrays.asList(sourceTopic));

    if (disablePeriodicCommits) {
      options = options.commitInterval(Duration.ZERO);
    }

    return new ReactiveKafkaConsumerTemplate<>(options);
  }
}
