package org.thecuriousdev.reactivewebcouchbase.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class User extends CouchbaseDocument {

  private String name;
  private int age;

  @Override
  public String getType() {
    return "user";
  }
}
