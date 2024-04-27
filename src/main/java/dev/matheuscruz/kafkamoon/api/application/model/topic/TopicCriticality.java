package dev.matheuscruz.kafkamoon.api.application.model.topic;

import dev.matheuscruz.kafkamoon.api.application.model.exception.InvalidCriticalityException;
import java.util.Arrays;

public enum TopicCriticality {
  TEST(1, (short) 1);

  private final Integer partitions;
  private final Short replicationFactor;

  TopicCriticality(Integer partitions, Short replicationFactor) {
    this.partitions = partitions;
    this.replicationFactor = replicationFactor;
  }

  public Integer getPartitions() {
    return partitions;
  }

  public Short getReplicationFactor() {
    return replicationFactor;
  }

  public static TopicCriticality fromString(String criticality) {
    try {
      return valueOf(criticality);
    } catch (IllegalArgumentException e) {
      throw new InvalidCriticalityException(
          "Invalid criticality name, allowed values are: %s".formatted(Arrays.toString(values())));
    }
  }
}
