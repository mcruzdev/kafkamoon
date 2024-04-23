package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import org.apache.kafka.clients.admin.Config;

public interface KafkaClient {
   Config createTopic(String topicName, Integer partitions, Short replicationFactor);
}
