package com.meli.shortener.url.services.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.meli.shortener.url.services.application.service.LoadEntryUseCase;
import com.meli.shortener.url.services.domain.UrlEntry;
import com.meli.shortener.url.services.application.port.out.GetEntry;
import com.meli.shortener.url.services.adapters.filter.HashBloomFilter;
import java.nio.charset.Charset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

class LoadEntryUseCaseTest {

  @Mock
  private GetEntry getEntry;

  @Mock
  private HashBloomFilter hashBloomFilter;

  @InjectMocks
  private LoadEntryUseCase loadEntryUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindById() {
    UrlEntry entry = UrlEntry.builder().id("123").url("http://example.com").hash("abcd1234").active(true).build();
    when(getEntry.findById("123")).thenReturn(entry);

    UrlEntry result = loadEntryUseCase.findById("123");
    assertEquals(entry, result);
  }

  @Test
  void testFindByHashForRedirect_NotFound() {
    BloomFilter<String> bloom = BloomFilter.create(
        Funnels.stringFunnel(Charset.forName("UTF-8")),
        1_000_000, 0.01);
    when(hashBloomFilter.getEntrySet()).thenReturn(bloom);
    when(hashBloomFilter.exist("abc")).thenReturn(false);

    assertThrows(ResponseStatusException.class, () -> {
      loadEntryUseCase.findByHashForRedirect("abc");
    });
  }

  @Test
  void testFindByHashForRedirect_Inactive() {
    BloomFilter<String> bloom = BloomFilter.create(
        Funnels.stringFunnel(Charset.forName("UTF-8")),
        1_000_000, 0.01);
    when(hashBloomFilter.getEntrySet()).thenReturn(bloom);
    UrlEntry entry = UrlEntry.builder().hash("abc").active(false).build();
    when(hashBloomFilter.exist("abc")).thenReturn(true);
    when(getEntry.findByHash("abc")).thenReturn(entry);

    assertThrows(ResponseStatusException.class, () -> {
      loadEntryUseCase.findByHashForRedirect("abc");
    });
  }
}
