package com.simplerest;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestTest {

  private static final String BASE_URL = "https://api.restful-api.dev";
  private static Client client;
  private static String createdObjectId;

  @BeforeAll
  public static void setupClient() throws Exception {
    var okhttpbuilder = new OkHttpClient.Builder();
    okhttpbuilder.cookieJar(new CookieSession());
    okhttpbuilder.addInterceptor(new LoggingInterceptor());
    var clientOption = ClientOption.builder()
      .baseUrl(BASE_URL)
      .httpClient(okhttpbuilder.build())
      .build();

    client = new Client(clientOption);
  }

  @Test
  @Order(1)
  public void testPostRequest() throws Exception {
    var product = new Product();
    product.name = "Test Product POST";
    product.data = new ProductData();
    product.data.color = "Blue";
    product.data.capacity = "128GB";
    product.data.cpuModel = "Intel Core i5";

    var response = client.post(product, "/objects", Product.class);

    assertNotNull(response);
    assertNotNull(response.id);
    assertNotNull(response.name);

    createdObjectId = response.id;

    System.out.println("POST Success - Created ID: " + response.id);
  }

  @Test
  @Order(2)
  public void testGetRequest() throws Exception {
    assertNotNull(
      createdObjectId,
      "Object ID harus udah dibuat dari POST test"
    );

    var response = client.get("/objects/" + createdObjectId, Product.class);

    assertNotNull(response);
    assertNotNull(response.id);
    assertEquals(createdObjectId, response.id);

    System.out.println("GET Success - Retrieved ID: " + response.id);
  }

  @Test
  @Order(3)
  public void testPutRequest() throws Exception {
    assertNotNull(
      createdObjectId,
      "Object ID harus udah dibuat dari POST test"
    );

    var product = new Product();
    product.name = "Updated Product PUT";
    product.data = new ProductData();
    product.data.color = "Red";
    product.data.capacity = "256GB";
    product.data.cpuModel = "Intel Core i7";

    var response = client.put(
      product,
      "/objects/" + createdObjectId,
      Product.class
    );

    assertNotNull(response);
    assertEquals("Updated Product PUT", response.name);

    System.out.println("PUT Success - Updated Name: " + response.name);
  }

  @Test
  @Order(4)
  public void testPatchRequest() throws Exception {
    assertNotNull(
      createdObjectId,
      "Object ID harus udah dibuat dari POST test"
    );

    var product = new Product();
    product.name = "Patched Product";
    product.data = new ProductData();
    product.data.color = "Green";

    var response = client.patch(
      product,
      "/objects/" + createdObjectId,
      Product.class
    );

    assertNotNull(response);
    assertEquals("Patched Product", response.name);

    System.out.println("PATCH Success - Patched Name: " + response.name);
  }

  @Test
  @Order(5)
  public void testDeleteRequest() throws Exception {
    assertNotNull(
      createdObjectId,
      "Object ID harus udah dibuat dari POST test"
    );

    var response = client.delete(
      "/objects/" + createdObjectId,
      DeleteResponse.class
    );

    assertNotNull(response);
    assertNotNull(response.message);

    System.out.println("DELETE Success - Message: " + response.message);
  }
}

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
class Product {

  String id;
  String name;
  ProductData data;
  String createdAt;
  String updatedAt;
}

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
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

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
class DeleteResponse {

  String message;
}
