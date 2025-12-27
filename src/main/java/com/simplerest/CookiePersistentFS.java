package com.simplerest;

import java.util.*;
import okhttp3.*;

// TODO: Implement CookiePersistentFS class
public class CookiePersistentFS implements CookieJar {

  @Override
  public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {}

  @Override
  public List<Cookie> loadForRequest(HttpUrl url) {
    return null;
  }
}
