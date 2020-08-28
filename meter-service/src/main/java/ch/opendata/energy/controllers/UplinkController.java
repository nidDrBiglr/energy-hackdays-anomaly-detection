package ch.opendata.energy.controllers;

import ch.opendata.energy.domain.meter.MeterUplinkEvent;
import ch.opendata.energy.domain.meter.MeterUplinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class UplinkController {
  private final MeterUplinkService uplinkService;

  public UplinkController(MeterUplinkService uplinkService) {
    this.uplinkService = uplinkService;
  }

  @PostMapping("/up")
  public Mono<Object> up(@RequestBody MeterUplinkEvent payload) {
    return uplinkService.sendMessage(payload).map(r -> "");
  }
}
