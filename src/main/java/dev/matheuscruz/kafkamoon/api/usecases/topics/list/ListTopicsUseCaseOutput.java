package dev.matheuscruz.kafkamoon.api.usecases.topics.list;

public record ListTopicsUseCaseOutput(String name, String topicId, boolean internal) {
}
