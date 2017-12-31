package io.vertx.starter;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import java.util.Arrays;

public class Triggers {
  static Observable<TriggerData[]> getTemperatureProperties(WebClient client, JsonObject reqBody) {
    JsonObject triggerFields = reqBody.getJsonObject("triggerFields");
    String thngId = triggerFields.getString("device");

    Single<HttpResponse<Property[]>> thngProperties = client
      .get("/thngs/" + thngId + "/properties/temperature")
      .putHeader("Authorization", System.getenv("API_KEY"))
      .as(BodyCodec.json(Property[].class))
      .rxSend();

    Single<HttpResponse<JsonObject>> thng = client
      .get("/thngs/" + thngId)
      .putHeader("Authorization", System.getenv("API_KEY"))
      .as(BodyCodec.jsonObject())
      .rxSend();

    return Single.zip(thngProperties, thng, (props, thngData) -> {
      String deviceName = thngData.body().getString("name");

      return Arrays.stream(props.body()).map(property -> {
        return new TriggerData(deviceName, property);
      }).toArray(TriggerData[]::new);
    }).toObservable();
  }
}
