package com.simplerest;

import java.util.*;
import okhttp3.*;

public class CookieSession implements CookieJar {

  private List<Cookie> cookies;

  @Override
  public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    this.cookies = cookies;
  }

  @Override
  public List<Cookie> loadForRequest(HttpUrl url) {
    if (cookies == null) {
      cookies = new ArrayList<Cookie>();
    }

    return cookies;
  }
}
