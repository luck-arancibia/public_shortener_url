package com.meli.shortener.url.services.config.swagger;

import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "commons.http.swagger")
@Getter
@Setter
@Validated
public class SwaggerProperties {

  @NotNull
  @Valid
  private ApiInfoProperties apiInfo = new ApiInfoProperties();

  @Size(min = 0)
  @Valid
  private List<TagProperty> tags = Collections.emptyList();

  private List<String> customHeadersResponse;
  private List<HeaderProperty> customHeadersRequest;

  @Setter
  @Getter
  public static class TagProperty {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

  }

  @Setter
  @Getter
  public static class HeaderProperty {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private Boolean required = false;

  }

}
