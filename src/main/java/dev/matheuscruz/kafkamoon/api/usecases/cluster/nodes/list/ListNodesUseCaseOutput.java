package dev.matheuscruz.kafkamoon.api.usecases.cluster.nodes.list;

public record ListNodesUseCaseOutput(
    String id,
    String host,
    int port,
    boolean hasRack,
    String rack,
    boolean isEmpty,
    Integer quantityOfTopics) {}
