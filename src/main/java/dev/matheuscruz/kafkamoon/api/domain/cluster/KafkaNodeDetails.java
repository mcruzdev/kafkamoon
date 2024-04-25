package dev.matheuscruz.kafkamoon.api.domain.cluster;

public record KafkaNodeDetails(
    String id,
    String host,
    boolean hasRack,
    String rack,
    int port,
    boolean isEmpty,
    Integer quantityOfTopics) {}
