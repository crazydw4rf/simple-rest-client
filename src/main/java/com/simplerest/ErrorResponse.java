package com.simplerest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {

  private Integer code;
  private String message;

  @JsonProperty("is_expected")
  private Boolean isExpected;
}
