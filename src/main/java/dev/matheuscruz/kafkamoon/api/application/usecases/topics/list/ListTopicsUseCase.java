package dev.matheuscruz.kafkamoon.api.application.usecases.topics.list;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * {@link ListTopicsUseCase} represents a use case. This use case lists all topics from Kafka cluster.
 */
@Component
public class ListTopicsUseCase {

  private final KafkaClient kafkaClient;
  private final Metrics metrics;

  public ListTopicsUseCase(KafkaClient kafkaClient, Metrics metrics) {
    this.kafkaClient = kafkaClient;
    this.metrics = metrics;
  }

  public List<ListTopicsUseCaseOutput> execute() {
    this.metrics.increment(MetricName.LIST_TOPICS, Tag.statusInit());
    List<ListTopicsUseCaseOutput> output =
        this.kafkaClient.listTopics().stream()
            .map(
                item ->
                    new ListTopicsUseCaseOutput(
                        item.name(), item.topicId().toString(), item.isInternal()))
            .toList();
    this.metrics.increment(MetricName.LIST_TOPICS, Tag.statusSuccess());
    return output;
  }
}
