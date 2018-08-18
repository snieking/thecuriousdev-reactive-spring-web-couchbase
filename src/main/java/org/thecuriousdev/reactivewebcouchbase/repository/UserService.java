package org.thecuriousdev.reactivewebcouchbase.repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.query.N1qlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thecuriousdev.reactivewebcouchbase.domain.User;
import org.thecuriousdev.reactivewebcouchbase.util.CouchbaseConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Observable;

@Service
public class UserService {

  private Bucket bucket;
  private CouchbaseConverter couchbaseConverter;

  @Autowired
  public UserService(Bucket bucket) {
    this.bucket = bucket;
    this.couchbaseConverter = new CouchbaseConverter(bucket.name(), User.class);
  }

  public Mono<User> findById(String id) {
    return couchbaseConverter.toMono(bucket.async().get(id));
  }

  public Flux<User> findAll() {
    return couchbaseConverter.n1qlQueryResultToFlux(bucket.async()
        .query(N1qlQuery.simple("SELECT * FROM `" + bucket.name() + "` WHERE type = \"user\"")));
  }

  public Mono<User> create(User user) {
    Observable<JsonDocument> observable = bucket.async().upsert(
        couchbaseConverter.toCouchbaseDocument(user.getName(), user));
    return couchbaseConverter.toMono(observable);
  }

}
