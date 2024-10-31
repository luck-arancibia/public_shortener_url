package com.meli.shortener.url.services.adapters.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import java.nio.charset.Charset;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Data
public class HashBloomFilter {

  private BloomFilter<String> entrySet;

  public void init() {
    this.entrySet = BloomFilter.create(
        Funnels.stringFunnel(Charset.forName("UTF-8")),
        1_000_000, 0.01);
  }

  public void add(String key) {
    this.entrySet.put(key);
  }

  public Boolean exist(String key) {
    return this.entrySet.mightContain(key);
  }

  public void delete() {
    //start over
    this.init();
  }

}
