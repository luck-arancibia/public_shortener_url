package com.meli.shortener.url.services.adapters.persistence;

import com.meli.shortener.url.services.domain.UrlEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends MongoRepository<UrlEntry, String> {

  UrlEntry save(UrlEntry urlEntry);

  boolean existsById(String id);

  void deleteById(String id);

  UrlEntry insert(UrlEntry urlEntry);

  List<UrlEntry> findAll();

  Optional<UrlEntry> findByHash(String hash);

  Optional<UrlEntry> findByUrl(String url);

  Optional<UrlEntry> findById(String id);
}
