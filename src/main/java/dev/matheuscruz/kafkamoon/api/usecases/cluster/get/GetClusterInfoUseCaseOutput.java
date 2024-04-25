package dev.matheuscruz.kafkamoon.api.usecases.cluster.get;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetClusterInfoUseCaseOutput(
    String id, String host, boolean hasHack, String hack, int port, boolean isEmpty) {}
