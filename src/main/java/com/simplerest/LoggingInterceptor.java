package com.simplerest;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Slf4j
public class LoggingInterceptor implements Interceptor {

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    Request request = chain.request();

    long t1 = System.nanoTime();
    log.debug("Sending request {}\n", request.url());

    Response response = chain.proceed(request);

    long t2 = System.nanoTime();
    log.debug(String.format(
        "Received response for %s in %.1fms%n%scode: %d\n",
        response.request().url(),
        (t2 - t1) / 1e6d,
        response.headers(),
        response.code()));

    return response;
  }
}
