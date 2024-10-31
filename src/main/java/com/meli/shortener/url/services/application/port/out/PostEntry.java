package com.meli.shortener.url.services.application.port.out;

import com.meli.shortener.url.services.domain.UrlEntry;

public interface PostEntry {

  UrlEntry postByUrl(UrlEntry urlEntry);
}
