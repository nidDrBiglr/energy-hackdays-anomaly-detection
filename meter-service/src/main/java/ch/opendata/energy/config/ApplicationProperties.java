package ch.opendata.energy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
  private MongoDBProperties mongodbProperties;
}

