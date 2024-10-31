package com.meli.shortener.url.services.application.service;

import com.meli.shortener.url.services.application.port.in.DestroyEntry;
import com.meli.shortener.url.services.application.port.out.DeleteEntry;
import com.meli.shortener.url.services.application.port.out.GetEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DestroyEntryUseCase implements DestroyEntry {

  private final DeleteEntry deleteEntry;
  private final GetEntry getEntry;

  @Override
  public void destroy(String id) {
    UrlEntry entry = getEntry.findById(id);
    if (entry == null) {
      return;
    }
    deleteEntry.deleteById(id);
  }
}
