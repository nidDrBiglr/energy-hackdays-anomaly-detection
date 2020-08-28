package ch.opendata.energy.domain.meter;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "data")
@Data
@Builder
public class MeterDocument {
  @Id
  private String id;
  private Instant timestamp;
  private String meterId;
  @JsonProperty("kWh")
  private Double kWh;

  @CreatedDate
  private Instant dateStored;
}

