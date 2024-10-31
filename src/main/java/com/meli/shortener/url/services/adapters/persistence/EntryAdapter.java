package com.meli.shortener.url.services.adapters.persistence;

import com.meli.shortener.url.services.application.port.out.DeleteEntry;
import com.meli.shortener.url.services.application.port.out.GetEntry;
import com.meli.shortener.url.services.application.port.out.PostEntry;
import com.meli.shortener.url.services.application.port.out.PutEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EntryAdapter implements GetEntry, PostEntry, DeleteEntry, PutEntry {

  private final EntryRepository urlEntryRepository;

  @Override
  public UrlEntry findById(String id) {
    return urlEntryRepository.findById(id).orElse(null);
  }

  @Override
  public UrlEntry findByHash(String hash) {
    return urlEntryRepository.findByHash(hash).orElse(null);
  }

  @Override
  public UrlEntry findByUrl(String url) {
    return urlEntryRepository.findByUrl(url).orElse(null);
  }

  @Override
  public List<UrlEntry> getAll() {
    return urlEntryRepository.findAll();
  }

  @Override
  public UrlEntry postByUrl(UrlEntry urlEntry) {
    return urlEntryRepository.insert(urlEntry);
  }

  @Override
  public void deleteById(String id) {
    urlEntryRepository.deleteById(id);
  }

  @Override
  public UrlEntry put(UrlEntry urlEntry) {
    return urlEntryRepository.save(urlEntry);
  }
}