package io.vertx.starter;

public class Property<T> {

  public String getKey() {
    return key;
  }

  public T getValue() {
    return value;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  private String key;
  private T value;
  private long timestamp;
  private long createdAt;

  Property(String key, T value, long timestamp, long createdAt) {
    this.key = key;
    this.value = value;
    this.timestamp = timestamp;
    this.createdAt = createdAt;
  }

  Property() {}
}
