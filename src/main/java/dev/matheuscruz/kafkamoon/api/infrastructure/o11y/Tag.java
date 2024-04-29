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

  public static Tag statusError() {
    return new Tag("status", "error");
  }

  /**
   * Method responsible for validating {@link Tag} value object.
   * @return <code>true</code> if is valid, <code>false</code> if invalid.
   */
  public boolean selfValidate() {
    return this.isNotBlank(this.key) && this.isNotBlank(this.value);
  }

  private boolean isNotBlank(final String value) {
    return value != null && !value.isBlank();
  }
}
