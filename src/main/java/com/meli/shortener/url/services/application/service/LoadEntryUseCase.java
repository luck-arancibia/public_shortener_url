package com.meli.shortener.url.services.application.service;

import com.meli.shortener.url.services.adapters.filter.HashBloomFilter;
import com.meli.shortener.url.services.application.port.in.LoadEntry;
import com.meli.shortener.url.services.application.port.out.GetEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import com.newrelic.api.agent.NewRelic;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class LoadEntryUseCase implements LoadEntry {

  private final GetEntry getEntry;
  private final HashBloomFilter hashBloomFilter;

  @Override
  public UrlEntry findById(String id) {
    return getEntry.findById(id);
  }

  @Override
  public UrlEntry findByHash(String hash) {
    return getEntry.findByHash(hash);
  }

  @SneakyThrows
  @Override
  public UrlEntry findByHashForRedirect(String hash) {
    UrlEntry urlEntry;
    if (hashBloomFilter.getEntrySet().mightContain(hash)) {
      urlEntry = this.findByHash(hash);
    } else {
      NewRelic.addCustomParameter("result", "not_found");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }
    if (urlEntry.getActive().equals(Boolean.FALSE)) {
      NewRelic.addCustomParameter("result", "not_active");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not active");
    }
    NewRelic.addCustomParameter("result", "founded");
    return urlEntry;
  }

  @Override
  public UrlEntry findByUrl(String url) {
    return getEntry.findByUrl(url);
  }

  @Override
  public List<UrlEntry> getAll() {
    return getEntry.getAll();
  }
}
