package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DefaultKafkaClient implements KafkaClient {

   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKafkaClient.class);

   private final Properties props;

   public DefaultKafkaClient(final Properties props) {
      this.props = props;
   }

   public Config createTopic(String topicName, Integer partitions, Short replicationFactor) {
      LOGGER.info("[flow:create.topic] Creating topic with name {}", topicName);
      try (AdminClient adminClient = AdminClient.create(this.props)) {
         CreateTopicsResult result = adminClient.createTopics(List.of(
               new NewTopic(topicName, Optional.ofNullable(partitions), Optional.ofNullable(replicationFactor))));
         return result.config(topicName).get(); // wait until
      } catch (ExecutionException | InterruptedException e) {
         if (e.getCause() != null && e.getCause() instanceof TopicExistsException) {
            throw new TopicExistsException("Topic with name %s already exists".formatted(topicName), e);
         }
         throw new RuntimeException(e);
      }
   }

   public Collection<TopicListing> listTopics() {
      LOGGER.info("[flow:topic.list] Listing topics from Kafka bootstrap servers");
      try (AdminClient adminClient = AdminClient.create(this.props)) {
         ListTopicsResult result = adminClient.listTopics();
         return result.listings().get(); // blocking call
      } catch (ExecutionException | InterruptedException e) {
         //         TODO: handle exception
         throw new RuntimeException(e);
      }
   }
}
