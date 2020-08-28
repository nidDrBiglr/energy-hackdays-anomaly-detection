package ch.opendata.energy.config;

import lombok.Data;

@Data
public class MongoDBProperties {
  private String uri;
  private String database;
  private String collectionName;
}
