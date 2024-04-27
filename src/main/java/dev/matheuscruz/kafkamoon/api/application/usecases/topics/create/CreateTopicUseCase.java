package dev.matheuscruz.kafkamoon.api.application.usecases.topics.create;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import dev.matheuscruz.kafkamoon.api.model.topic.TopicCriticality;
import dev.matheuscruz.kafkamoon.api.model.topic.TopicName;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link CreateTopicUseCase} represents the topic creation use case.
 */
@Component
public class CreateTopicUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateTopicUseCase.class);
  private final KafkaClient kafkaClient;
  private final Metrics metrics;

  public CreateTopicUseCase(KafkaClient kafkaClient, Metrics metrics) {
    this.kafkaClient = kafkaClient;
    this.metrics = metrics;
  }

  public CreateTopicUseCaseOutput execute(final CreateTopicUseCaseInput input) {
    metrics.increment(MetricName.CREATE_TOPIC, Tag.statusInit());

    TopicName topicName = new TopicName(input.messageType(), input.dataSet(), input.dataName());

    TopicCriticality criticality =
        TopicCriticality.fromString(input.criticality().toUpperCase(Locale.ROOT));

    LOGGER.info(
        "[flow:create.topic][criticality:{}] Create topic with name {}, partitions {} and"
            + " replication factor {}",
        criticality.name(),
        topicName,
        criticality.getPartitions(),
        criticality.getReplicationFactor());

    String topicId =
        this.kafkaClient.createTopic(
            topicName.finalName(), criticality.getPartitions(), criticality.getReplicationFactor());
    LOGGER.info(
        "[flow:create.topic][status:success] Topic with name '{}' has the id '{}'",
        topicName,
        topicId);

    metrics.increment(MetricName.CREATE_TOPIC, Tag.create("status", "success"));

    return new CreateTopicUseCaseOutput(topicName.finalName(), topicId);
  }
}
