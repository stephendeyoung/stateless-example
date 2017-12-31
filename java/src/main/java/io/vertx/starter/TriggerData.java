package io.vertx.starter;

import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class TriggerData<T> {
  private String device;
  private T value;
  private String createdAt;
  private Map meta = new HashMap<String, T>();

  TriggerData(String thngName, Property prop) {
    this.device = thngName;
    this.value = (T) prop.getValue();
    this.createdAt = String.valueOf(prop.getTimestamp());

    meta.put("id", String.valueOf(prop.getTimestamp()));
    meta.put("timestamp", prop.getTimestamp());
  }

}
