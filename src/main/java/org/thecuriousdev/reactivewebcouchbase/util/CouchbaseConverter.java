package org.thecuriousdev.reactivewebcouchbase.util;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.thecuriousdev.reactivewebcouchbase.domain.CouchbaseDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;

public class CouchbaseConverter<T extends CouchbaseDocument> {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private final String bucketName;
  private final Class<T> clazz;

  public CouchbaseConverter(String bucketName, Class<T> clazz) {
    this.bucketName = bucketName;
    this.clazz = clazz;
  }

  public Mono<T> toMono(Observable<JsonDocument> observable) {
    var publisher = RxReactiveStreams.toPublisher(observable);
    return Mono.from(publisher)
        .map(document -> {
          JsonObject jsonObj = document.content();
          T obj = parseObject(jsonObj);
          obj.setId(document.id());
          obj.setCas(document.cas());
          obj.setExpiry(document.expiry());
          return obj;
        });
  }

  public JsonDocument toCouchbaseDocument(String id, T obj) {
    try {
      return JsonDocument.create(id, obj.getExpiry(),
          JsonObject.fromJson(MAPPER.writeValueAsString(obj)), obj.getCas());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public Flux<T> n1qlQueryResultToFlux(Observable<AsyncN1qlQueryResult> query) {
    var publisher = RxReactiveStreams.toPublisher(query.flatMap(result -> result.rows()));

    return Flux.from(publisher)
        .map(row -> row.value().get(bucketName))
        .map(this::parseObject);
  }

  private T parseObject(Object obj) {
    try {
      return MAPPER.readValue(obj.toString(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
