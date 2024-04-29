package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.get;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetClusterInfoUseCaseOutput(
    String id, String host, boolean hasRack, String rack, int port, boolean isEmpty) {}
