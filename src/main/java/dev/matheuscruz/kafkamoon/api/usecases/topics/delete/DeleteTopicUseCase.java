package dev.matheuscruz.kafkamoon.api.usecases.topics.delete;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import org.springframework.stereotype.Component;

@Component
public class DeleteTopicUseCase {

   private final KafkaClient kafkaClient;

   public DeleteTopicUseCase(KafkaClient kafkaClient) {
      this.kafkaClient = kafkaClient;
   }

   public void execute(String topicUUid) {
      this.kafkaClient.deleteTopic(topicUUid);
   }
}
