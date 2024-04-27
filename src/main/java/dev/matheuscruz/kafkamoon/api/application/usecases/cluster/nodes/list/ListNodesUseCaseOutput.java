package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.nodes.list;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListNodesUseCaseOutput(
    String id,
    String host,
    int port,
    boolean hasRack,
    String rack,
    boolean isEmpty,
    Integer quantityOfTopics) {}
