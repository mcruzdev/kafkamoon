package dev.matheuscruz.kafkamoon.api.application.usecases.topics.list;

public record ListTopicsUseCaseOutput(String name, String topicId, boolean internal) {}
