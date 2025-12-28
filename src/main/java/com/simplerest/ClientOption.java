package com.simplerest;

import okhttp3.*;

public class ClientOption {

  private String baseUrl;
  private OkHttpClient httpClient = null;

  private ClientOption(Builder builder) {
    this.baseUrl = builder.baseUrl;
    this.httpClient = builder.httpClient;
  }

  public static Builder builder() {
    return new Builder();
  }

  String getBaseUrl() {
    return baseUrl;
  }

  OkHttpClient getHttpClient() {
    return httpClient;
  }

  public static class Builder {

    private String baseUrl = "http://localhost";
    private OkHttpClient httpClient = new OkHttpClient();

    public Builder httpClient(OkHttpClient client) {
      this.httpClient = client;
      return this;
    }

    public Builder baseUrl(String base) {
      this.baseUrl = base;
      return this;
    }

    public ClientOption build() throws Exception {
      if (httpClient == null) {
        var builder = new OkHttpClient.Builder();
        httpClient = builder.build();
      } else if (baseUrl.isBlank()) {
        throw new Exception("baseUrl gak boleh kosong");
      }

      return new ClientOption(this);
    }
  }
}
