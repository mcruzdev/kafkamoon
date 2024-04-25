package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaClusterInfo;
import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaNodeDetails;
import java.util.Collection;
import java.util.List;
import org.apache.kafka.clients.admin.TopicListing;

public interface KafkaClient {
  String createTopic(String topicName, Integer partitions, Short replicationFactor);

  Collection<TopicListing> listTopics();

  void deleteTopic(String topicUUid);

  KafkaClusterInfo getClusterInfo();

  List<KafkaNodeDetails> getNodes();
}
