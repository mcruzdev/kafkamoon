package dev.matheuscruz.kafkamoon.api.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RedirectSwaggerUI {

  @GetMapping
  public ResponseEntity<Object> redirect() {
    return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
        .header("Location", "/swagger-ui.html")
        .build();
  }
}
