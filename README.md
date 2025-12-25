# Simple REST Client

Cuma HTTP client wrapper yang simpel.

## Instalasi

Download JAR pada halaman [release](https://github.com/crazydw4rf/simple-rest-client/releases) kemudian import atau tambahkan ke classpath project.

## Cara Pakai

### Setup Client

```java
import com.simplerest.HTTPClient.*;

// Buat client baru
var builder = ClientBuilder.builder()
    .baseUrl("https://api.example.com")
    .build();

var client = new Client(builder);
```

### POST Request

```java
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class User {
  String id;
  String name;
  int age;
}

// Buat object yang akan dikirim
var data = new User();
data.name = "Ucup";
data.age = 20;

// Kirim POST request
try {
    var response = client.post(data, "/api/users", User.class);
    // tipe data response = User
    System.out.println(response);
} catch (RestClientException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### Custom OkHttpClient

Kalau mau custom OkHttp client sendiri:

```java
var okhttp = new OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build();

var builder = ClientBuilder.builder()
    .baseUrl("https://api.example.com")
    .httpClient(okhttp)
    .build();

var client = new Client(builder);
```

## Requirement

- Java 17+
- OkHttp3 5.x
- Jackson 3.x
