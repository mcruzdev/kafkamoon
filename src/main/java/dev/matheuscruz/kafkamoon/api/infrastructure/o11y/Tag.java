package dev.matheuscruz.kafkamoon.api.infrastructure.o11y;

public record Tag(String key, String value) {

  public static Tag create(String key, String value) {
    return new Tag(key, value);
  }

  public static Tag statusInit() {
    return new Tag("status", "init");
  }

  public static Tag statusSuccess() {
    return new Tag("status", "success");
  }
}
