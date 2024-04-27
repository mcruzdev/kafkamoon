package dev.matheuscruz.kafkamoon.api.application.usecases.topics.delete;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import org.springframework.stereotype.Component;

/**
 * {@link DeleteTopicByIdUseCase} represents a use case. This use case deletes topic from id by from Kafka cluster.
 */
@Component
public class DeleteTopicByIdUseCase {

  private final KafkaClient kafkaClient;
  private final Metrics metrics;

  public DeleteTopicByIdUseCase(KafkaClient kafkaClient, Metrics metrics) {
    this.kafkaClient = kafkaClient;
    this.metrics = metrics;
  }

  public void execute(String topicUUid) {
    this.metrics.increment(MetricName.DELETE_TOPIC, Tag.statusInit());
    this.kafkaClient.deleteTopic(topicUUid);
    this.metrics.increment(MetricName.DELETE_TOPIC, Tag.statusSuccess());
  }
}
