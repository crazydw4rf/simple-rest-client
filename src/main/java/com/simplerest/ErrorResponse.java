package com.simplerest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

  private int code;
  private String message;

  @JsonProperty("is_expected")
  private Boolean isExpected;

  public ErrorResponse() {}

  public ErrorResponse(int code, String message, boolean isExpected) {
    this.code = code;
    this.message = message;
    this.isExpected = isExpected;
  }
}
