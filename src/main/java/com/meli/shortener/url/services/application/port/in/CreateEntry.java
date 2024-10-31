package com.meli.shortener.url.services.application.port.in;

import com.meli.shortener.url.services.domain.UrlEntry;

public interface CreateEntry {

  UrlEntry postByUrl(String url);
}
