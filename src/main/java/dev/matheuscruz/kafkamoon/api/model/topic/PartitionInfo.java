package dev.matheuscruz.kafkamoon.api.model.topic;

import dev.matheuscruz.kafkamoon.api.model.cluster.KafkaNodeDetails;
import java.util.List;

public record PartitionInfo(
    int partition, KafkaNodeDetails leader, List<KafkaNodeDetails> replicas) {}
