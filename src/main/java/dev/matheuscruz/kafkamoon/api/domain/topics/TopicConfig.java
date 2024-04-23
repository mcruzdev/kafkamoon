package dev.matheuscruz.kafkamoon.api.domain.topics;

public record TopicConfig(Integer partition, Short replicationFactor) {
}
