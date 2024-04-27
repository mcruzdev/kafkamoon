package dev.matheuscruz.kafkamoon.api.usecases.topics.create;

import dev.matheuscruz.kafkamoon.api.domain.topics.TopicCriticality;
import dev.matheuscruz.kafkamoon.api.domain.topics.TopicName;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Use case class that represents the to create topic use case.
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
        TopicCriticality.valueOf(input.criticality().toUpperCase(Locale.ROOT));

    LOGGER.info(
        "[flow:create.topic] Create topic with name {}, partitions {} and replication factor {}",
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
