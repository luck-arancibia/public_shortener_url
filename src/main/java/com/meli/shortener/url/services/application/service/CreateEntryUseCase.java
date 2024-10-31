package com.meli.shortener.url.services.application.service;

import com.meli.shortener.url.services.adapters.filter.HashBloomFilter;
import com.meli.shortener.url.services.application.port.in.CreateEntry;
import com.meli.shortener.url.services.application.port.out.PostEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateEntryUseCase implements CreateEntry {

  private final Environment environment;
  private final PostEntry postEntry;
  private final HashBloomFilter hashBloomFilter;


  @Override
  public UrlEntry postByUrl(String url) {
    String hash = uniqueHash(url);
    UrlEntry urlEntry = UrlEntry.builder()
        .shortUrl(generateRedirectUri(hash))
        .url(url)
        .hash(hash)
        .build();
    hashBloomFilter.add(hash);
    return postEntry.postByUrl(urlEntry);
  }

  private String uniqueHash(String url) {
    String hash = generateCustomHash();
    if (hashBloomFilter.exist(hash)) {
      return uniqueHash(url);
    } else {
      return hash;
    }
  }

  public static String generateCustomHash() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  private String generateRedirectUri(String hash) {
    String port = environment.getProperty("local.server.port");
    String host = "http://localhost";
    if (Arrays.asList(environment.getActiveProfiles()).contains("production")) {
      host = "http://144.126.213.134";
    }
    return host + ":" +  port + "/" + hash;
  }
}
