package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

public class KafkaCommunicationException extends RuntimeException {
  public KafkaCommunicationException(Throwable cause) {
    super("Error while communication with Kafka cluster, please try again later.", cause);
  }
}
