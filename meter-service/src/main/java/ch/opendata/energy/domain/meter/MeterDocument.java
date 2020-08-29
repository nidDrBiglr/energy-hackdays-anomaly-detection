package ch.opendata.energy.domain.meter;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterDocument {
  @Id
  @JsonIgnore
  private String id;
  private Instant timestamp;
  private String meterId;
  @JsonProperty("kWh")
  private Double kWh;

  @CreatedDate
  private Instant dateStored;
}

