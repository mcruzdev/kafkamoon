package dev.matheuscruz.kafkamoon.api.application.usecases.topics.delete;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import org.springframework.stereotype.Component;

/**
 * {@link DeleteTopicByIdUseCase} represents a use case. This use case deletes topic from id by from Kafka cluster.
 */
@Component
public class DeleteTopicByIdUseCase {

  private final KafkaClient kafkaClient;

  public DeleteTopicByIdUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public void execute(String id) {
    this.kafkaClient.deleteTopic(id);
  }
}
