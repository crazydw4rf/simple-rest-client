package com.simplerest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplerest.HTTPClient.*;
import okhttp3.*;
import tools.jackson.databind.ObjectMapper;

public class Main {

  private static final String BASE_URL = "https://api.restful-api.dev";
  private static final MediaType JSON = MediaType.get("application/json");
  private static final ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws Exception {
    var builder = new OkHttpClient.Builder();
    var client = builder.build();

    var reqGet = new Request.Builder()
      .url(
        String.format(
          "%s/%s",
          BASE_URL,
          "objects/ff8081819782e69e019b4e920c9c66b0"
        )
      )
      .build();

    try (var res = client.newCall(reqGet).execute()) {
      String strBody = res.body().string();

      Product p1 = mapper.readValue(strBody, Product.class);

      System.out.println("Product ID: " + p1.id);
      System.out.println("Product Name: " + p1.name);
      System.out.println("Product Color: " + p1.data.color);
      System.out.println("Product Capacity: " + p1.data.capacity);
    }

    // POST
    var p2 = new Product();
    p2.data = new ProductData();
    p2.name = "Ipong 77";
    p2.data.color = "red";
    p2.data.capacity = "128";
    p2.data.cpuModel = "Intel Core i15";

    var body = RequestBody.create(mapper.writeValueAsBytes(p2), JSON);
    var reqPost = new Request.Builder()
      .url(
        String.format(
          "%s/%s",
          BASE_URL,
          "objects/ff8081819782e69e019b4e920c9c66b0"
        )
      )
      .patch(body)
      .build();

    try (var res = client.newCall(reqPost).execute()) {
      String strBody = res.body().string();
      System.out.println(strBody);
    }

    var b = ClientBuilder.builder();
    b.baseUrl(BASE_URL).build();

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
