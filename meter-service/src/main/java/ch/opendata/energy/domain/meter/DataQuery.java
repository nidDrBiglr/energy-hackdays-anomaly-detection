package ch.opendata.energy.domain.meter;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DataQuery {
  @NotNull
  private String meterId;
  @NotNull
  private Date from;
  @NotNull
  private Date to;
}
