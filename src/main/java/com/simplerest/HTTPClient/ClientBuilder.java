package com.simplerest.HTTPClient;

import okhttp3.OkHttpClient;

public class ClientBuilder {

  private String baseUrl;
  private OkHttpClient httpClient = null;

  public static ClientBuilder builder() {
    return new ClientBuilder();
  }

  String getBaseUrl() {
    return baseUrl;
  }

  OkHttpClient getHttpClient() {
    return httpClient;
  }

  public ClientBuilder httpClient(OkHttpClient client) {
    this.httpClient = client;
    return this;
  }

  public ClientBuilder baseUrl(String base) {
    this.baseUrl = base;
    return this;
  }

  public Client build() throws Exception {
    if (httpClient == null) {
      httpClient = new OkHttpClient();
    } else if (baseUrl.isBlank()) {
      throw new Exception("baseUrl gak boleh kosong");
    }

    return new Client(this);
  }
}
