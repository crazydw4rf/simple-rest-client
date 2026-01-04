package com.simplerest;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

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
    var url = buildUrl(path);
    log.debug("New GET request created with:\nurl: {}", url);

    Request request = new Request.Builder().url(buildUrl(path)).get().build();

    return executeRequest(request, cls);
  }

  public <T> T post(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var url = buildUrl(path);
    log.debug(
      "New POST request created with:\nurl: {}\nbody: {}\n",
      url,
      payload
    );

    RequestBody body = createJsonBody(payload);
    Request request = new Request.Builder().url(url).post(body).build();

    return executeRequest(request, cls);
  }

  public <T> T put(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var url = buildUrl(path);
    log.debug(
      "New PUT request created with:\nurl: {}\nbody: {}\n",
      url,
      payload
    );

    RequestBody body = createJsonBody(payload);
    Request request = new Request.Builder().url(url).put(body).build();

    return executeRequest(request, cls);
  }

  public <T> T patch(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var url = buildUrl(path);
    log.debug(
      "New PATCH request created with:\nurl: {}\nbody: {}\n",
      url,
      payload
    );

    RequestBody body = createJsonBody(payload);
    Request request = new Request.Builder().url(url).patch(body).build();

    return executeRequest(request, cls);
  }

  public <T> T delete(String path, Class<T> cls) throws RestClientException {
    var url = buildUrl(path);
    log.debug("New DELETE request created with:\nurl: {}\n", url);

    Request request = new Request.Builder().url(url).delete().build();

    return executeRequest(request, cls);
  }

  private <T> T executeRequest(Request request, Class<T> cls)
    throws RestClientException {
    try (Response response = httpClient.newCall(request).execute()) {
      String responseBody = response.body().string();

      if (!response.isSuccessful()) {
        throw new RestClientException(
          request.url().toString(),
          String.format(
            "%s request failed with status code: %d, body: %s",
            request.method(),
            response.code(),
            responseBody
          ),
          null
        );
      }

      return mapper.readValue(responseBody, cls);
    } catch (RestClientException e) {
      throw e;
    } catch (Exception e) {
      throw new RestClientException(
        request.url().toString(),
        String.format("%s request error: %s", request.method(), e.getMessage()),
        e
      );
    }
  }

  private String buildUrl(String path) {
    return baseUrl.concat(path);
  }

  private RequestBody createJsonBody(Object payload)
    throws RestClientException {
    try {
      String json = mapper.writeValueAsString(payload);
      return RequestBody.create(json, JSON);
    } catch (Exception e) {
      throw new RestClientException(
        baseUrl,
        "Failed to serialize payload to JSON: " + e.getMessage(),
        e
      );
    }
  }
}
