package ch.opendata.energy.controllers;

import ch.opendata.energy.domain.meter.DataQuery;
import ch.opendata.energy.domain.meter.MeterDocument;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;

@RestController
public class DataController {
  @Autowired
  private MongoClient mongoClient;

  @PostMapping("/data")
  public Flux<Document> getData(@RequestBody @Valid DataQuery query) {
    return Flux.from(getDataCollection().aggregate(mongoDBPipeline(query)));
  }

  @PostMapping("/anomalies")
  public Flux<Document> getAnomalies(@RequestBody @Valid DataQuery query) {
    return Flux.from(getAnomaliesCollection().aggregate(mongoDBPipeline(query)));
  }

  @PostMapping("/forecast")
  public Flux<MeterDocument> getForecast(@RequestBody @Valid DataQuery query) {
    // TODO: 29.08.20
    return Flux.empty();
  }

  private List<Bson> mongoDBPipeline(DataQuery query) {
    return Arrays.asList(
      // set filters to match the query params
      Aggregates.match(
        and(
          Filters.eq("meterId", query.getMeterId()),
          Filters.lte("timestamp", query.getTo()),
          Filters.gte("timestamp", query.getFrom())
        )
      ),
      Aggregates.project(
        Projections.fields(
          Projections.excludeId(),
          Projections.exclude("_class")
        )
      )
    );
  }

  private MongoCollection<Document> getDataCollection() {
    MongoDatabase database = mongoClient
      .getDatabase("meter_data");
    return database.getCollection("data");
  }

  private MongoCollection<Document> getAnomaliesCollection() {
    MongoDatabase database = mongoClient
      .getDatabase("meter_data");
    return database.getCollection("anomalies");
  }

}
