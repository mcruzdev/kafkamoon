package dev.matheuscruz.kafkamoon.api.domain.cluster;

public record KafkaClusterInfo(
    String id, String host, boolean hasRack, String rack, int port, boolean isEmpty) {}
