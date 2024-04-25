package dev.matheuscruz.kafkamoon.api.usecases.topics.create;

import java.time.OffsetDateTime;

public record CreateTopicUseCaseOutput(
    String topicName, String topicId, OffsetDateTime createdAt) {}
