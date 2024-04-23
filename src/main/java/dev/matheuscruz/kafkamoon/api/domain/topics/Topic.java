package dev.matheuscruz.kafkamoon.api.domain.topics;


import dev.matheuscruz.kafkamoon.api.infrastructure.persistence.converter.TopicNameConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "topics")
public class Topic {

   @Id
   @Column(name = "id")
   private String id;
   @Column(name = "name")
   @Convert(converter = TopicNameConverter.class)
   private TopicName name;
   @Column(name = "created_at")
   private OffsetDateTime createdAt;
   @Column(name = "updated_at")
   private OffsetDateTime updatedAt;
   @Column(name = "deleted_at")
   private OffsetDateTime deletedAt;

   public Topic(TopicName topicName) {
      this.id = UUID.randomUUID().toString();
      final OffsetDateTime now = OffsetDateTime.now();
      this.updatedAt = now;
      this.createdAt = now;
      this.name = topicName;
   }

   public String getId() {
      return id;
   }

   public TopicName getName() {
      return name;
   }

   public OffsetDateTime getCreatedAt() {
      return createdAt;
   }

   public OffsetDateTime getUpdatedAt() {
      return updatedAt;
   }

   public OffsetDateTime getDeletedAt() {
      return deletedAt;
   }
}

