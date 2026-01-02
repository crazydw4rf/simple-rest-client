package com.simplerest;

import lombok.Builder;
import lombok.Getter;
import okhttp3.*;

@Builder
@Getter(lombok.AccessLevel.PACKAGE)
public class ClientOption {

  @Builder.Default
  private String baseUrl = "http://localhost";

  @Builder.Default
  private OkHttpClient httpClient = new OkHttpClient();
}
