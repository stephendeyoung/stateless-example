package io.vertx.starter;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.*;

@RunWith(VertxUnitRunner.class)
public class TriggersTest {

  private Vertx vertx;
//
  @Before
  public void setUp(TestContext tc) {
    //MockitoAnnotations.initMocks(this);
    vertx = Vertx.vertx();
  }
//
  @After
  public void tearDown(TestContext tc) {
    vertx.close(tc.asyncAssertSuccess());
  }

//  @Mock
//  WebClient client;
//
//  @Mock
//  HttpRequest<Buffer> request;
//
//  @Mock
//  HttpRequest<Property[]> propertyRequest;
//
//  @Mock
//  HttpResponse<Property[]> response;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8000);

  @Test
  public void temperatureTriggerReturnsTriggerData(TestContext tc) {
    Gson gson = new Gson();
    Async async = tc.async();
    WebClientOptions options = new WebClientOptions()
      .setDefaultPort(8000);
    WebClient client = WebClient.create(vertx, options);

    String thngId = "1";
    String thngName = "test";
    Property mockProperty = new Property("temperature", 25, Instant.now().toEpochMilli(), Instant.now().toEpochMilli());
    Property[] mockPropertyData = {mockProperty};

    stubFor(get(urlEqualTo("/thngs/" + thngId + "/properties/temperature"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(gson.toJson(mockPropertyData, Property[].class))));

    stubFor(get(urlEqualTo("/thngs/" + thngId))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"name\": \"" + thngName + "\"}")));

//    when(client.get("/thngs/" + thngId + "/properties/temperature")).thenReturn(request);
//    when(request.putHeader(anyString(), anyString())).thenReturn(request);
//    when(request.as(any())).thenReturn(propertyRequest);
//    when(propertyRequest.rxSend()).thenReturn(Single.just(response));
//    when(response.body()).thenReturn(mockPropertyData);

    JsonObject requestBody = new JsonObject();
    JsonObject triggerFields = new JsonObject();

    triggerFields.put("device", thngId);

    requestBody.put("triggerFields", triggerFields);

    Observable<TriggerData[]> propertyData = Triggers.getTemperatureProperties(client, requestBody);
    TriggerData[] expectedData = {new TriggerData(thngName, mockProperty)};

    propertyData.subscribe(triggerData -> {
      tc.assertTrue(triggerData.length == 1);
      Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedData[0], triggerData[0]));
      async.complete();
    }, tc::fail);
//    Async async = tc.async();
//    vertx.createHttpClient().getNow(8080, "localhost", "/", request -> {
//      tc.assertEquals(request.statusCode(), 200);
//      request.bodyHandler(body -> {
//        tc.assertTrue(body.length() > 0);
//        async.complete();
//      });
//    });
  }

}
