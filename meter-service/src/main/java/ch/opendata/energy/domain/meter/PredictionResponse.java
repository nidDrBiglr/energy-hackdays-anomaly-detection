package ch.opendata.energy.domain.meter;

import lombok.Data;

@Data
public class PredictionResponse {
  private boolean success;
  private String label;
  private String severity;
}
