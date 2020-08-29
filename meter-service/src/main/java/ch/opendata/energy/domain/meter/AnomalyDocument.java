package ch.opendata.energy.domain.meter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "anomalies")
@Data
@Builder
public class AnomalyDocument {
  @Id
  private String id;
  private Instant timestamp;
  @JsonProperty("kWh")
  private Double kWh;
  private String meterId;
}
