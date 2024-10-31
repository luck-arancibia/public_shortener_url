package com.meli.shortener.url.services.application.port.out;

import com.meli.shortener.url.services.domain.UrlEntry;

public interface PutEntry {

  UrlEntry put(UrlEntry urlEntry);
}
