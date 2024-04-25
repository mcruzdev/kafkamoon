package dev.matheuscruz.kafkamoon.api.domain.topics;

public class TopicNameExceededException extends RuntimeException {

  public TopicNameExceededException(final String topicName) {
    super("The topic name '%s' exceeds the maximum allowed length.".formatted(topicName));
  }
}
