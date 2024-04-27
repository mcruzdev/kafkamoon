package dev.matheuscruz.kafkamoon.api.application.usecases.topics.list;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * {@link ListTopicsUseCase} represents a use case to list topics from Kafka cluster.
 */
@Component
public class ListTopicsUseCase {

  private final KafkaClient kafkaClient;

  public ListTopicsUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public List<ListTopicsUseCaseOutput> execute() {
    return this.kafkaClient.listTopics().stream()
        .map(
            item ->
                new ListTopicsUseCaseOutput(
                    item.name(), item.topicId().toString(), item.isInternal()))
        .toList();
  }
}
