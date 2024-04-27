package dev.matheuscruz.kafkamoon.api.application.usecases.topics.get;

import dev.matheuscruz.kafkamoon.api.application.model.cluster.KafkaNodeDetails;
import dev.matheuscruz.kafkamoon.api.application.model.topic.PartitionInfo;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.errors.UnknownTopicIdException;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * {@link GetTopicByIdUseCase} represents a use case. This use case gets topic by id from Kafka cluster.
 */
@Component
public class GetTopicByIdUseCase {

   private final KafkaClient kafkaClient;
   private final Metrics metrics;

   public GetTopicByIdUseCase(KafkaClient kafkaClient, Metrics metrics) {
      this.kafkaClient = kafkaClient;
      this.metrics = metrics;
   }

   public Predicate<Node> isNotLeaderId(String leaderId) {
      return id -> !leaderId.equals(id.idString());
   }

   public GetTopicByNameUseCaseOutput execute(String topicId) {
      this.metrics.increment(MetricName.GET_TOPIC_BY_ID, Tag.statusInit());
      TopicDescription topic = this.kafkaClient.getTopicById(topicId)
            .orElseThrow(() -> new UnknownTopicIdException("Topic with id %s does not exist".formatted(topicId)));

      GetTopicByNameUseCaseOutput output = new GetTopicByNameUseCaseOutput(topic.name(), topic.topicId().toString(),
            topic.partitions().stream().map(this::mapPartitionInfo).toList());

      metrics.increment(MetricName.GET_TOPIC_BY_ID, Tag.statusSuccess());

      return output;
   }

   private PartitionInfo mapPartitionInfo(final TopicPartitionInfo info) {
      String leaderId = info.leader().idString();
      return new PartitionInfo(info.partition(),
            new KafkaNodeDetails(leaderId, info.leader().host(), info.leader().hasRack(), info.leader().rack(),
                  info.leader().port(), info.leader().isEmpty(), null),
            info.replicas().stream().filter(isNotLeaderId(leaderId))
                  .map(r -> new KafkaNodeDetails(r.idString(), r.host(), r.hasRack(), r.rack(), r.port(), r.isEmpty(),
                        null)).toList());
   }
}
