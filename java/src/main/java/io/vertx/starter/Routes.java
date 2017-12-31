package io.vertx.starter;

import com.google.gson.Gson;
import io.reactivex.Observable;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;

public class Routes {
  static void setupRoutes(Router router, WebClient client) {
    Gson gson = new Gson();

    router.route(HttpMethod.POST, "/ifttt/v1/triggers/temperature").handler(requestContext -> {
      Observable<TriggerData[]> triggerData = Triggers.getTemperatureProperties(client, requestContext.getBodyAsJson());

      HttpServerResponse response = requestContext.response();

      response.setChunked(true);
      response.putHeader("Content-Type", "application/json");

      triggerData.subscribe(
        allData -> response.write(gson.toJson(allData, TriggerData[].class)),
        err -> {
          System.out.println(err);
          err.printStackTrace();
        },
        () -> {
          response.end();
        }
      );
    });
  }
}
