package com.meli.shortener.url.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UrlEntry")
@AllArgsConstructor
@Builder
@Data
public class UrlEntry {

  @Id
  private String id;
  @NotNull
  @Indexed
  private String url;
  @Indexed
  @NotNull
  private String hash;
  @JsonProperty("short_url")
  private String shortUrl;
  @Default
  private Boolean active = true;
}