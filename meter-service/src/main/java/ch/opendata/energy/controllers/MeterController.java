package ch.opendata.energy.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class MeterController {
  @PostMapping("/meters/{meterId}/data")
  public Mono<Object> echo(@PathVariable("meterId") String meterId, @RequestBody Object query) {
    return Mono.just(new Object());
  }
}

