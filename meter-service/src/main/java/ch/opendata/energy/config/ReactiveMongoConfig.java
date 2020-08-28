package ch.opendata.energy.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration(proxyBeanMethods = false)
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {
  @Autowired
  private ApplicationProperties applicationProperties;

  @Bean
  @Override
  public MongoClient reactiveMongoClient() {
    MongoDBProperties properties = applicationProperties.getMongodbProperties();
    if (properties == null || properties.getUri() == null) {
      throw new IllegalStateException("app.mongodb-properties.uri can't be null");
    }

    return MongoClients.create(applicationProperties.getMongodbProperties().getUri());
  }

  @Override
  protected String getDatabaseName() {
    MongoDBProperties properties = applicationProperties.getMongodbProperties();
    if (properties == null || properties.getDatabase() == null) {
      throw new IllegalStateException("app.mongodb-properties.database can't be null");
    }

    return applicationProperties.getMongodbProperties().getDatabase();
  }
}
