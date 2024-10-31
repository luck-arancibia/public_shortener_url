package com.meli.shortener.url.services.adapters.web;

import com.meli.shortener.url.services.application.port.in.LoadEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@Api(tags = {"GetAndRedirectEntry"})
@RestController
@RequiredArgsConstructor
public class GetAndRedirectController {

  private final LoadEntry loadEntry;

  @ApiOperation(
      value = "get and redirect",
      nickname = "GetAndRedirect")
  @GetMapping("/{hash}")
  @Trace(dispatcher = true)
  public RedirectView getAndRedirect(@PathVariable("hash") String hash) {
    NewRelic.addCustomParameter("hash", hash);
    UrlEntry urlEntry = loadEntry.findByHashForRedirect(hash);
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(urlEntry.getUrl());
    return redirectView;
  }
}
