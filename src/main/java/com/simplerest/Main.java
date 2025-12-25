package com.simplerest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplerest.HTTPClient.*;
import tools.jackson.databind.ObjectMapper;

public class Main {

  private static final String BASE_URL = "https://api.restful-api.dev";
  private static final ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws Exception {
    var b = ClientBuilder.builder();
    b.baseUrl(BASE_URL).build();

    var p2 = new Product();
    p2.name = "Ipong 99";
    p2.data = new ProductData();
    p2.data.cpuModel = "Intel Core i7";
    p2.data.hardDiskSize = "1TB";
    p2.data.color = "Black";
    p2.data.capacity = "256GB";

    var httpClient = new Client(b);
    var obj = httpClient.post(
      p2,
      "/objects/ff8081819782e69e019b4e920c9c66b0",
      Product.class
    );
    System.out.println(mapper.writeValueAsString(obj));

    System.exit(0);
  }
}

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class Product {

  // @JsonProperty("id")
  String id;

  // @JsonProperty("name")
  String name;

  // @JsonProperty("data")
  ProductData data;

  String createdAt;
}

// @JsonAutoDetect(fieldVisibility = Visibility.ANY)
class ProductData {

  @JsonProperty("CPU model")
  String cpuModel;

  @JsonProperty("Hard disk size")
  String hardDiskSize;

  @JsonProperty("color")
  String color;

  @JsonProperty("capacity")
  String capacity;
}
