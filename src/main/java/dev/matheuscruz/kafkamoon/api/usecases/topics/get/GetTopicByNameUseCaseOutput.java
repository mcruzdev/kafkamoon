package dev.matheuscruz.kafkamoon.api.usecases.topics.get;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.matheuscruz.kafkamoon.api.domain.topics.PartitionInfo;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetTopicByNameUseCaseOutput(
    String name, String id, List<PartitionInfo> partitionInfos) {}
