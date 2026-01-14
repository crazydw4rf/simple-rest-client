package com.simplerest;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public class Client {

  private final MediaType JSON = MediaType.get("application/json");
  private final ObjectMapper mapper = new ObjectMapper();
  private final OkHttpClient httpClient;
  private final String baseUrl;

  public Client(ClientOption option) {
    this.baseUrl = option.getBaseUrl();
    this.httpClient = option.getHttpClient();
  }

  public <T> T get(String path, Class<T> cls) throws RestClientException {
    Request request = new Request.Builder().url(buildUrl(path)).get().build();
    return executeRequest(request, cls);
  }

  public <T> T post(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var url = buildUrl(path);
    RequestBody body = createJsonBody(payload);
    Request request = new Request.Builder().url(url).post(body).build();

    return executeRequest(request, cls);
  }

  public <T> T put(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var url = buildUrl(path);
    RequestBody body = createJsonBody(payload);
    Request request = new Request.Builder().url(url).put(body).build();

    return executeRequest(request, cls);
  }

  public <T> T patch(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var url = buildUrl(path);
    RequestBody body = createJsonBody(payload);
    Request request = new Request.Builder().url(url).patch(body).build();

    return executeRequest(request, cls);
  }

  public <T> T delete(String path, Class<T> cls) throws RestClientException {
    var url = buildUrl(path);
    Request request = new Request.Builder().url(url).delete().build();

    return executeRequest(request, cls);
  }

  private <T> T executeRequest(Request request, Class<T> cls)
    throws RestClientException {
    try (Response response = httpClient.newCall(request).execute()) {
      String responseBody = response.body().string();

      if (!response.isSuccessful()) {
        try {
          var root = mapper.readTree(responseBody);
          var error = mapper.treeToValue(
            root.at("/error"),
            ErrorResponse.class
          );
          throw new RestClientException(error);
        } catch (JacksonException e) {
          throw new RestClientException(
            "Can't parse error response: " + e.getMessage()
          );
        }
      }

      return mapper.readValue(responseBody, cls);
    } catch (IOException | JacksonException e) {
      throw new RestClientException(e.getMessage());
    }
  }

  private String buildUrl(String path) {
    return baseUrl + path;
  }

  private RequestBody createJsonBody(Object payload)
    throws RestClientException {
    try {
      String json = mapper.writeValueAsString(payload);
      return RequestBody.create(json, JSON);
    } catch (Exception e) {
      throw new RestClientException(e.getMessage());
    }
  }
}
