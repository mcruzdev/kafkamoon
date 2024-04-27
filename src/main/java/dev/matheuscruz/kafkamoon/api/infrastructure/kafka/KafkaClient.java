package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaNodeDetails;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface KafkaClient {
  String createTopic(String topicName, Integer partitions, Short replicationFactor);

  Collection<TopicListing> listTopics();

  void deleteTopic(String topicUUid);

  KafkaNodeDetails getClusterInfo();

  List<KafkaNodeDetails> getNodes();

  Optional<TopicDescription> getTopicByName(String id);
}
