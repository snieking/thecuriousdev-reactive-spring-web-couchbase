package org.thecuriousdev.reactivewebcouchbase.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class CouchbaseDocument {

  @JsonIgnore private String id;
  @JsonIgnore private long cas;
  @JsonIgnore private int expiry;
  private String type;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getCas() {
    return cas;
  }

  public void setCas(long cas) {
    this.cas = cas;
  }

  public int getExpiry() {
    return expiry;
  }

  public void setExpiry(int expiry) {
    this.expiry = expiry;
  }

  public void setType(String type) {
    this.type = type;
  }

  public abstract String getType();

}
