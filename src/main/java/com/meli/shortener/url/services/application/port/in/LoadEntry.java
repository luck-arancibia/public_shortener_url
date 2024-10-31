package com.meli.shortener.url.services.application.port.in;

import com.meli.shortener.url.services.domain.UrlEntry;
import java.util.List;

public interface LoadEntry {

  UrlEntry findById(String id);

  UrlEntry findByHash(String hash);

  UrlEntry findByHashForRedirect(String hash);

  UrlEntry findByUrl(String url);

  List<UrlEntry> getAll();
}
