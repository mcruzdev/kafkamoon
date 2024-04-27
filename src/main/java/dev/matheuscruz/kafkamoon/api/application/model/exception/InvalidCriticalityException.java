package dev.matheuscruz.kafkamoon.api.application.model.exception;

public class InvalidCriticalityException extends RuntimeException {
  public InvalidCriticalityException(String message) {
    super(message);
  }
}
