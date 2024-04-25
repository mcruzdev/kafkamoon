package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import java.util.Collection;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.TopicListing;

public interface KafkaClient {
  Config createTopic(String topicName, Integer partitions, Short replicationFactor);

  Collection<TopicListing> listTopics();

  void deleteTopic(String topicUUid);
}
