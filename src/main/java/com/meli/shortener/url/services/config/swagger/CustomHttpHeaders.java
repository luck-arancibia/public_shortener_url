package com.meli.shortener.url.services.config.swagger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomHttpHeaders {

  public static final String APPLICATION_NAME = "X-Application-Name";
  public static final String CORRELATION_ID = "X-Correlation-Id";
  public static final String FORWARDED_FOR = "X-Forwarded-For";
}
