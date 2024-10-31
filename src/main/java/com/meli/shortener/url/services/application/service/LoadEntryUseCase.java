package com.meli.shortener.url.services.application.service;

import com.meli.shortener.url.services.adapters.filter.HashBloomFilter;
import com.meli.shortener.url.services.application.port.in.LoadEntry;
import com.meli.shortener.url.services.application.port.out.GetEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
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
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }
    if (urlEntry.getActive().equals(Boolean.FALSE)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not active");
    }
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

//#  user_key: 'NRAK-F77JMJQGI8CRH0O7Y0LM2IG5R46'
//license_key: 'c9c3c999223e051690e945898dcbebc6FFFFNRAL'