package com.meli.shortener.url.services.application.port.out;

import com.meli.shortener.url.services.domain.UrlEntry;
import java.util.List;

public interface GetEntry {

  UrlEntry findById(String id);

  UrlEntry findByHash(String hash);

  UrlEntry findByUrl(String url);

  List<UrlEntry> getAll();
}
