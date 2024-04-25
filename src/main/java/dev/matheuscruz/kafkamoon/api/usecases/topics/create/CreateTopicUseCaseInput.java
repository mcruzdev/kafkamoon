package dev.matheuscruz.kafkamoon.api.usecases.topics.create;

public record CreateTopicUseCaseInput(
    String messageType, String dataSet, String dataName, String criticality) {}
