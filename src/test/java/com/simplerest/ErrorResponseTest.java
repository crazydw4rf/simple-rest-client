package com.simplerest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class ErrorResponseTest {

  private static final String BASE_URL = "http://localhost:8080";
  private static Client client;

  @BeforeAll
  public static void setupClient() throws Exception {
    var okhttpbuilder = new OkHttpClient.Builder();
    okhttpbuilder.cookieJar(new CookieSession());
    okhttpbuilder.addInterceptor(new LoggingInterceptor());
    var clientOption = ClientOption.builder()
      .baseUrl(BASE_URL)
      .httpClient(okhttpbuilder.build())
      .build();

    client = new Client(clientOption);
  }

  @ToString
  public class Foo {

    String message;
  }

  @Test
  @Order(1)
  public void testErrorResponse() {
    try {
      var response = client.get("/error", Foo.class);
      log.debug("Response: {}", response);

      assertNotNull(response);
    } catch (RestClientException e) {
      ErrorResponse error = e.getError();
      log.debug("Error: {}", error);
      assertNotNull(error);

      assertNotNull(error.getMessage());
      assertTrue(error.getCode() >= 400);
    }
  }
}
