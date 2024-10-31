package com.meli.shortener.url.services.config.swagger;

import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

@Getter
@Setter
public class ApiInfoProperties {

  private String title = "";
  private String description = "";
  private String version = "";
  private String termsOfServiceUrl = "";
  private SwaggerContactProperties contact = new SwaggerContactProperties();
  private String license = "";
  private String licenseUrl = "";

  @Setter
  @Getter
  public class SwaggerContactProperties {

    private String name = "";
    private String url = "";
    private String email = "";

  }

  public ApiInfo getObject() {

    return new ApiInfo(
        title,
        description,
        version,
        termsOfServiceUrl,
        new Contact(
            contact.getName(),
            contact.getUrl(),
            contact.getEmail()
        ),
        license,
        licenseUrl,
        Collections.emptyList()
    );
  }

}
