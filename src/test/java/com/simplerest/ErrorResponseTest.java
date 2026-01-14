package com.simplerest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class ErrorResponseTest {

  private static final String BASE_URL = "http://localhost:8080";
  private static Client client;
  private static ObjectMapper mapper = new ObjectMapper();
  private WireMockServer wms;

  static {
    var okhttpbuilder = new OkHttpClient.Builder();
    okhttpbuilder.cookieJar(new CookieSession());
    okhttpbuilder.addInterceptor(new LoggingInterceptor());
    var clientOption = ClientOption.builder()
      .baseUrl(BASE_URL)
      .httpClient(okhttpbuilder.build())
      .build();

    client = new Client(clientOption);
  }

  @BeforeEach
  public void beforeEach() throws Exception {
    wms = new WireMockServer(8080);
    wms.start();
    configureFor("localhost", wms.port());
  }

  @AfterEach
  public void afterEach() throws Exception {
    wms.stop();
  }

  @Test
  @Order(1)
  public void testErrorResponse() {
    var err = new ErrorResponse(404, "Error message", false);
    var payloadObj = mapper.createObjectNode();
    payloadObj.putPOJO("error", err);

    String payload = null;
    try {
      payload = mapper.writeValueAsString(payloadObj);
    } catch (Exception ignore) {}

    assertNotNull(payload);

    log.info("Payload: {}", payload);

    wms.stubFor(
      get("/not-found").willReturn(
        notFound()
          .withHeader("Content-Type", "application/json")
          .withBody(payload)
      )
    );

    try {
      var response = client.get("/not-found", String.class);
      log.debug("Response: {}", response);

      assertNotNull(response);
    } catch (RestClientException e) {
      ErrorResponse error = e.getError();
      log.debug("Error: {}", error);
      assertNotNull(error);

      assertNotNull(error.getMessage());
      assertEquals(404, error.getCode());
    }
  }
}
