package com.meli.shortener.url.services.adapters.web;

import com.meli.shortener.url.services.application.port.in.CreateEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"PostEntry"})
@RestController
@RequiredArgsConstructor
public class PostUrlEntryController {

  private final CreateEntry createEntry;

  @ApiOperation(
      value = "post new entry",
      nickname = "PostEntry",
      response = UrlEntry.class)
  @PostMapping(
      value = "/entry",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Trace(dispatcher = true)
  public ResponseEntity<UrlEntry> postEntry(
      @RequestBody @Validated UrlDto urlDto) {
    UrlEntry urlEntry = createEntry.postByUrl(urlDto.ensureProtocol());
    NewRelic.addCustomParameter("hash", urlEntry.getHash());
    return ResponseEntity.ok(urlEntry);
  }

  @Data
  static private class UrlDto {

    @NotBlank(message = "URL is required")
    @Size(min = 3, max = 200)
    @Pattern(regexp = "(\\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", message = "URL invalid")
    private String url;

    private String ensureProtocol() {
      URI uri = URI.create(this.url);
      if (uri.getScheme() == null) {
        return "https://" + uri;
      }
      return uri.toString();
    }
  }
}
