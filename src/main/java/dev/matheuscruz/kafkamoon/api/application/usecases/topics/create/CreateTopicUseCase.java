package dev.matheuscruz.kafkamoon.api.application.usecases.topics.create;

import dev.matheuscruz.kafkamoon.api.application.model.topic.TopicCriticality;
import dev.matheuscruz.kafkamoon.api.application.model.topic.TopicName;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link CreateTopicUseCase} represents a use case. This use case creates a topic on Kafka cluster.
 */
@Component
public class CreateTopicUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateTopicUseCase.class);
  private final KafkaClient kafkaClient;

  public CreateTopicUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public CreateTopicUseCaseOutput execute(final CreateTopicUseCaseInput input) {

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

    return new CreateTopicUseCaseOutput(topicName.finalName(), topicId);
  }
}
