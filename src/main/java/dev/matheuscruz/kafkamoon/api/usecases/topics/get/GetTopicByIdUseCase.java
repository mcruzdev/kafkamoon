package dev.matheuscruz.kafkamoon.api.usecases.topics.get;

import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaNodeDetails;
import dev.matheuscruz.kafkamoon.api.domain.topics.PartitionInfo;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import java.util.function.Predicate;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.errors.UnknownTopicIdException;
import org.springframework.stereotype.Component;

@Component
public class GetTopicByIdUseCase {

  private final KafkaClient kafkaClient;

  public GetTopicByIdUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public Predicate<Node> isNotLeaderId(String leaderId) {
    return id -> !leaderId.equals(id.idString());
  }

  public GetTopicByNameUseCaseOutput execute(String topicId) {
    TopicDescription topic =
        this.kafkaClient
            .getTopicById(topicId)
            .orElseThrow(
                () ->
                    new UnknownTopicIdException(
                        "Topic with id %s does not exist".formatted(topicId)));

    return new GetTopicByNameUseCaseOutput(
        topic.name(),
        topic.topicId().toString(),
        topic.partitions().stream()
            .map(
                p -> {
                  String leaderId = p.leader().idString();

                  return new PartitionInfo(
                      p.partition(),
                      new KafkaNodeDetails(
                          leaderId,
                          p.leader().host(),
                          p.leader().hasRack(),
                          p.leader().rack(),
                          p.leader().port(),
                          p.leader().isEmpty(),
                          null),
                      p.replicas().stream()
                          .filter(isNotLeaderId(leaderId))
                          .map(
                              r ->
                                  new KafkaNodeDetails(
                                      r.idString(),
                                      r.host(),
                                      r.hasRack(),
                                      r.rack(),
                                      r.port(),
                                      r.isEmpty(),
                                      null))
                          .toList());
                })
            .toList());
  }
}
