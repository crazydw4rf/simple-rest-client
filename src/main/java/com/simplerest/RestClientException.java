package com.simplerest;

public class RestClientException extends Exception {

  public RestClientException(String url, String message, Throwable e) {
    super(String.format("[HTTP Client Error]: %s | %s\n", url, message), e);
  }

  public RestClientException(String message) {
    super(message);
  }
}
