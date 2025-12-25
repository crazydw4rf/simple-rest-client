package com.simplerest.HTTPClient;

import com.simplerest.RestClientException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tools.jackson.databind.ObjectMapper;

public class Client {

  private final MediaType JSON = MediaType.get("application/json");
  private final ObjectMapper mapper = new ObjectMapper();
  private final OkHttpClient httpClient;
  private final String baseUrl;

  public Client(ClientBuilder builder) {
    this.baseUrl = builder.getBaseUrl();
    this.httpClient = builder.getHttpClient();
  }

  public <T> T get(T cls) {
    return null;
  }

  public <T> T post(Object payload, String path, Class<T> cls)
    throws RestClientException {
    var urlPath = baseUrl.concat(path);
    var body = mapper.writeValueAsString(payload);
    var req = new Request.Builder()
      .url(urlPath)
      .patch(RequestBody.create(body, JSON))
      .build();

    try (var res = httpClient.newCall(req).execute()) {
      var resBody = res.body().string();
      var resClass = mapper.readValue(resBody, cls);
      return resClass;
    } catch (Exception e) {
      throw new RestClientException(
        urlPath,
        "Post request error",
        e.getCause()
      );
    }
  }

  public <T> T patch(T cls) {
    return null;
  }

  public <T> T delete(T cls) {
    return null;
  }

  public <T> T put(T cls) {
    return null;
  }
}
