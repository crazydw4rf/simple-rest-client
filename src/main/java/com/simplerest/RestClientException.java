package com.simplerest;

public class RestClientException extends Exception {

  private final ErrorResponse error;

  public RestClientException(String message) {
    super(message);
    this.error = null;
  }

  public RestClientException(ErrorResponse error) {
    super(error.getMessage());
    this.error = error;
  }

  public ErrorResponse getError() {
    return error;
  }
}
