package com.meli.shortener.url.services.adapters.web;

import com.meli.shortener.url.services.application.port.in.DestroyEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"DeleteEntry"})
@RestController
@RequiredArgsConstructor
public class DeleteEntryController {

  private final DestroyEntry destroyEntry;

  @ApiOperation(
      value = "delete entry",
      nickname = "DeleteEntry",
      response = Void.class)
  @DeleteMapping(
      value = "/entry",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteEntry(@RequestBody String id) {
    destroyEntry.destroy(id);
  }
}
