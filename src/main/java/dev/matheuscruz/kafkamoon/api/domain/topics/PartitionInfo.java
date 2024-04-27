package dev.matheuscruz.kafkamoon.api.domain.topics;

import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaNodeDetails;

import java.util.List;

public record PartitionInfo(int partition, KafkaNodeDetails leader, List<KafkaNodeDetails> replicas) {
}
