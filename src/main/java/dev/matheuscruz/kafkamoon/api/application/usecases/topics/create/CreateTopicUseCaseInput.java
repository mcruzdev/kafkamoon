package dev.matheuscruz.kafkamoon.api.application.usecases.topics.create;

public record CreateTopicUseCaseInput(
    String messageType, String dataSet, String dataName, String criticality) {}
