package dev.matheuscruz.kafkamoon.api.model.cluster;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record KafkaNodeDetails(
    String id,
    String host,
    boolean hasRack,
    String rack,
    int port,
    boolean isEmpty,
    Integer quantityOfTopics) {}
