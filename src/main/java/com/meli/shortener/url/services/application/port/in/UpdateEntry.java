package com.meli.shortener.url.services.application.port.in;

import com.meli.shortener.url.services.domain.UrlEntry;

public interface UpdateEntry {

  UrlEntry update(String id, String url, Boolean active);
}
