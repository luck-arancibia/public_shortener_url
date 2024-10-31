package com.meli.shortener.url.services.config.swagger;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

public class HandledHttpStatus {

  private static final HttpStatus[] list = {
      HttpStatus.OK,
      HttpStatus.CREATED,
      HttpStatus.NO_CONTENT,
      HttpStatus.BAD_REQUEST,
      HttpStatus.UNAUTHORIZED,
      HttpStatus.FORBIDDEN,
      HttpStatus.NOT_FOUND,
      HttpStatus.METHOD_NOT_ALLOWED,
      HttpStatus.UNSUPPORTED_MEDIA_TYPE,
      HttpStatus.UNPROCESSABLE_ENTITY,
      HttpStatus.INTERNAL_SERVER_ERROR
  };

  public static List<HttpStatus> getList() {
    return Arrays.asList(list);
  }

}
