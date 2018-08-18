package org.thecuriousdev.reactivewebcouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig {

  @Autowired
  private Cluster cluster;

  @Value("${spring.couchbase.bucket.name}")
  private String name;

  @Value("${spring.couchbase.bucket.password")
  private String password;

  @Bean
  public Bucket bucket() {
    return cluster.openBucket(name, password);
  }
}
