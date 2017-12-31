package io.vertx.starter;

import com.google.gson.Gson;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import java.lang.reflect.Array;
import java.util.Arrays;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    HttpServer server = vertx.createHttpServer();
    WebClientOptions options = new WebClientOptions()
      .setDefaultPort(80)
      .setDefaultHost("api.evrythng.com");
    WebClient client = WebClient.create(vertx, options);

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    Routes.setupRoutes(router, client);

    server.requestHandler(router::accept).listen(8080);
  }

}
