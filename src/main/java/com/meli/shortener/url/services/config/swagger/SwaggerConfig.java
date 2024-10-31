package com.meli.shortener.url.services.config.swagger;

import static java.util.function.Predicate.not;

import com.meli.shortener.url.services.config.swagger.SwaggerProperties.TagProperty;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.ModelSpecificationBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.Header;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  private static final List<String> HEADERS = List.of(
      HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
      HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
      HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
      HttpHeaders.CONTENT_DISPOSITION,
      HttpHeaders.CONTENT_LENGTH,
      HttpHeaders.CONTENT_TYPE,
      CustomHttpHeaders.APPLICATION_NAME,
      CustomHttpHeaders.CORRELATION_ID);

  private static final List<HttpMethod> REQUEST_METHODS = List.of(
      HttpMethod.GET,
      HttpMethod.POST,
      HttpMethod.PUT,
      HttpMethod.PATCH,
      HttpMethod.DELETE);
  private final SwaggerProperties swaggerProperties;

  @Bean
  public Docket api() {
    Docket docket = new Docket(DocumentationType.SWAGGER_2)
        .pathMapping("/")
        .apiInfo(swaggerProperties.getApiInfo().getObject());
    setGlobalResponseMessages(docket);
    docket = docket.select()
        .apis(not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
        .paths(PathSelectors.any())
        .build();
    for (TagProperty tag : swaggerProperties.getTags()) {
      docket.tags(new Tag(tag.getName(), tag.getDescription()));
    }
    setCustomRequestHeaders(docket);
    return docket;
  }

  private void setCustomRequestHeaders(Docket docket) {
    if (swaggerProperties.getCustomHeadersRequest() != null
        && !swaggerProperties.getCustomHeadersRequest().isEmpty()) {
      var parameters = new ArrayList<RequestParameter>();
      swaggerProperties.getCustomHeadersRequest().forEach(
          header ->
              parameters.add(new RequestParameterBuilder()
                  .name(header.getName())
                  .required(header.getRequired())
                  .description(header.getDescription())
                  .in(ParameterType.HEADER)
                  .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)))
                  .build()));
      docket.globalRequestParameters(parameters);
    }
  }

  private void setGlobalResponseMessages(Docket docket) {
    REQUEST_METHODS.forEach(method -> docket.globalResponses(method, getResponseMessages()));
  }

  private List<Response> getResponseMessages() {
    return HandledHttpStatus.getList().stream()
        .map(httpStatus -> new ResponseBuilder()
            .code(Integer.toString(httpStatus.value()))
            .description(httpStatus.getReasonPhrase())
            .headers(buildHeadersMap())
            .build())
        .collect(Collectors.toList());
  }

  private List<Header> buildHeadersMap() {
    List<String> headerComplete = new ArrayList<>(HEADERS);
    if (swaggerProperties.getCustomHeadersResponse() != null
        && !swaggerProperties.getCustomHeadersResponse().isEmpty()) {
      headerComplete.addAll(swaggerProperties.getCustomHeadersResponse());
    }
    var modelSpecification = (new ModelSpecificationBuilder()).scalarModel(ScalarType.STRING)
        .build();
    return headerComplete.stream()
        .map(
            header -> new Header(header, "", new ModelRef("string"), modelSpecification)
        ).collect(Collectors.toList());
  }

  @Bean
  public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
    return new BeanPostProcessor() {

      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName)
          throws BeansException {
        if (bean instanceof WebMvcRequestHandlerProvider) {
          customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
        }
        return bean;
      }

      private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
          List<T> mappings) {
        List<T> copy = mappings.stream()
            .filter(mapping -> mapping.getPatternParser() == null)
            .collect(Collectors.toList());
        mappings.clear();
        mappings.addAll(copy);
      }

      @SuppressWarnings("unchecked")
      private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
        try {
          Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
          field.setAccessible(true);
          return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          throw new IllegalStateException(e);
        }
      }
    };
  }

  @Bean
  public InternalResourceViewResolver defaultViewResolver() {
    return new InternalResourceViewResolver();
  }
}
