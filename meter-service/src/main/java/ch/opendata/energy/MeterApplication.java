package ch.opendata.energy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class MeterApplication {

  public static void main(String[] args) {
    SpringApplication.run(MeterApplication.class, args);
  }
}
