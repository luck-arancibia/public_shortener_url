package com.meli.shortener.url.services.adapters.web;

import com.meli.shortener.url.services.application.port.in.UpdateEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api(tags = {"PutEntry"})
@RestController
@RequiredArgsConstructor
public class PutEntryController {

  private final UpdateEntry updateEntry;

  @ApiOperation(
      value = "put entry",
      nickname = "PutEntry",
      response = UrlEntry.class)
  @PutMapping(
      value = "/entry",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UrlEntry> putEntry(@RequestBody EntryDto urlEntry) {
    UrlEntry entry = updateEntry.update(
        urlEntry.getId(),
        urlEntry.getUrl(),
        urlEntry.getActive()
    );
    if (entry == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }
    return ResponseEntity.ok(entry);
  }

  @Data
  static class EntryDto {

    private String id;
    private String url;
    private Boolean active;
  }
}
