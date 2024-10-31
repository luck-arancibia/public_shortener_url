package com.meli.shortener.url.services.adapters.web;

import com.meli.shortener.url.services.application.port.in.LoadEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"GetEntry"})
@RestController
@RequiredArgsConstructor
public class GetEntryController {

  private final LoadEntry loadEntry;

  @ApiOperation(
      value = "get all Entries",
      nickname = "GetAllEntries",
      responseContainer = "List",
      response = UrlEntry.class)
  @GetMapping(
      value = "/entry",
      produces = "application/json")
  public ResponseEntity<List<UrlEntry>> getEntries() {
    List<UrlEntry> urlEntries = loadEntry.getAll();
    return ResponseEntity.ok(urlEntries);
  }

  @ApiOperation(
      value = "get Entry detail",
      nickname = "GetEntryDetail",
      response = UrlEntry.class)
  @GetMapping(
      value = "/entry/{id}",
      produces = "application/json")
  public ResponseEntity<UrlEntry> getEntries(@PathVariable("id") String id) {
    return ResponseEntity.ok(loadEntry.findById(id));
  }
}
