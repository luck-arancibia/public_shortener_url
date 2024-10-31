package com.meli.shortener.url.services.application.service;

import com.meli.shortener.url.services.application.port.in.UpdateEntry;
import com.meli.shortener.url.services.application.port.out.GetEntry;
import com.meli.shortener.url.services.application.port.out.PutEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateEntryUseCase implements UpdateEntry {

  private final GetEntry getEntry;
  private final PutEntry putEntry;

  @Override
  public UrlEntry update(String id, String url, Boolean active) {
    UrlEntry entry = getEntry.findById(id);
    if (entry == null) {
      return null;
    }
    if ( active != null && entry.getActive() != active) {
      entry.setActive(active);
    }
    if (url != null && !entry.getUrl().equals(url)) {
      entry.setUrl(url);
    }
    return putEntry.put(entry);
  }
}
