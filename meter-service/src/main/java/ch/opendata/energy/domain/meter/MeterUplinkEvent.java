package ch.opendata.energy.domain.meter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeterUplinkEvent {
  private String meterId;
  private Instant timestamp;
  @JsonProperty("kWh")
  private Double kWh;
}
