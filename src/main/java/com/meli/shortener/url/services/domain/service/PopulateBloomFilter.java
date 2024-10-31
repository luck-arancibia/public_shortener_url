package com.meli.shortener.url.services.domain.service;

import com.meli.shortener.url.services.adapters.filter.HashBloomFilter;
import com.meli.shortener.url.services.application.port.in.LoadEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopulateBloomFilter {

  private final LoadEntry loadEntry;
  private final HashBloomFilter hashBloomFilter;

  public void init() {
    hashBloomFilter.init();
    loadEntry.getAll().stream().map(UrlEntry::getHash).forEach(hashBloomFilter::add);
  }
}
