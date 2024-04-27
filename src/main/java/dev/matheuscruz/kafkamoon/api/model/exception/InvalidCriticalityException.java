package dev.matheuscruz.kafkamoon.api.model.exception;

public class InvalidCriticalityException extends RuntimeException {
  public InvalidCriticalityException(String message) {
    super(message);
  }
}
