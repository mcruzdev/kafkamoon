package dev.matheuscruz.kafkamoon.api.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ping")
@RestController
public class PingController {

  @GetMapping(produces = { MediaType.TEXT_PLAIN_VALUE })
  public String ping() {
    return "pong";
  }
}
