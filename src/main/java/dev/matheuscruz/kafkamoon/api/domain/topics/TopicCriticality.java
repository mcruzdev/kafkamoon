package dev.matheuscruz.kafkamoon.api.domain.topics;

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
}
