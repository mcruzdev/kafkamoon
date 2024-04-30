package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import dev.matheuscruz.kafkamoon.api.application.model.cluster.KafkaNodeDetails;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import io.micrometer.core.annotation.Timed;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicCollection;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultKafkaClient implements KafkaClient {

   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKafkaClient.class);

   private final Properties props;
   private final Metrics metrics;

   public DefaultKafkaClient(final Properties props, Metrics metrics) {
      this.props = props;
      this.metrics = metrics;
   }

   @Timed(value = "kafka_client_create_topic_time")
   public String createTopic(String topicName, Integer partitions, Short replicationFactor) {
      LOGGER.info("[flow:create.topic] Creating topic with name {}", topicName);
      metrics.increment(MetricName.CREATE_TOPIC, Tag.statusInit());

      try (AdminClient adminClient = AdminClient.create(this.props)) {
         CreateTopicsResult result = adminClient.createTopics(List.of(
               new NewTopic(topicName, Optional.ofNullable(partitions), Optional.ofNullable(replicationFactor))));
         String generatedId = result.topicId(topicName).toCompletionStage().toCompletableFuture().join().toString();
         metrics.increment(MetricName.CREATE_TOPIC, Tag.statusSuccess());
         return generatedId;
      } catch (TopicExistsException | CompletionException | CancellationException e) {
         if (e.getCause() instanceof TopicExistsException) {
            String message = "Topic '%s' already exists.".formatted(topicName);
            LOGGER.warn("[flow:topic.create] {}", message);
            throw new TopicExistsException(message);
         }
         LOGGER.error("Error while creating topic with name '%s'".formatted(topicName), e);
         metrics.increment(MetricName.CREATE_TOPIC, Tag.statusError());
         throw new KafkaCommunicationException(e);
      }
   }

   @Timed(value = "kafka_client_list_topics_time")
   @Override
   public Collection<TopicListing> listTopics() {
      LOGGER.info("[flow:list.topic] Listing topics from Kafka bootstrap servers");
      this.metrics.increment(MetricName.LIST_TOPICS, Tag.statusInit());
      try (AdminClient adminClient = AdminClient.create(this.props)) {
         ListTopicsResult result = adminClient.listTopics();
         Collection<TopicListing> response = result.listings().toCompletionStage().toCompletableFuture().join();
         this.metrics.increment(MetricName.LIST_TOPICS, Tag.statusSuccess());
         return response;
      } catch (CompletionException | CancellationException e) {
         LOGGER.error("[flow:list.topic] Error while listing topics from Kafka cluster", e);
         this.metrics.increment(MetricName.LIST_TOPICS, Tag.statusError());
         throw new KafkaCommunicationException(e);
      }
   }

   @Override
   @Timed(value = "kafka_client_delete_topic_time")
   public void deleteTopic(String id) {
      LOGGER.info("[flow:topic.delete] Deleting topic with id '{}'", id);
      this.metrics.increment(MetricName.DELETE_TOPIC, Tag.statusInit());

      try (AdminClient adminClient = AdminClient.create(this.props)) {
         DeleteTopicsResult result = adminClient.deleteTopics(TopicCollection.ofTopicIds(List.of(Uuid.fromString(id))));
         result.all().toCompletionStage().toCompletableFuture().join();
         this.metrics.increment(MetricName.DELETE_TOPIC, Tag.statusSuccess());
      } catch (UnknownTopicIdException | CompletionException | CancellationException e) {
         skipIfNotUnknownTopicIdException(id, e);
      }
   }

   private void skipIfNotUnknownTopicIdException(String topicId, RuntimeException e) {
      if (!(e.getCause() instanceof UnknownTopicIdException)) {
         LOGGER.error("Error while deleting topic with id '%s'.".formatted(topicId), e);
         this.metrics.increment(MetricName.DELETE_TOPIC, Tag.statusError());
         throw new KafkaCommunicationException(e);
      }
      LOGGER.warn("[flow:topic.delete] Topic with id '{}' does not exist", topicId);
      this.metrics.increment(MetricName.DELETE_TOPIC, Tag.statusSuccess());
   }

   @Override
   @Timed(value = "kafka_get_cluster_info_time")
   public KafkaNodeDetails getClusterInfo() {
      LOGGER.info("[flow:cluster.get.info] Getting cluster info");
      this.metrics.increment(MetricName.GET_CLUSTER_INFO, Tag.statusInit());
      try (AdminClient adminClient = AdminClient.create(this.props)) {
         DescribeClusterResult result = adminClient.describeCluster();
         Node kafkaNode = result.controller().toCompletionStage().toCompletableFuture().join();
         KafkaNodeDetails kafkaNodeDetails = new KafkaNodeDetails(kafkaNode.idString(), kafkaNode.host(),
               kafkaNode.hasRack(), kafkaNode.rack(), kafkaNode.port(), kafkaNode.isEmpty(), null);
         this.metrics.increment(MetricName.GET_CLUSTER_INFO, Tag.statusSuccess());
         return kafkaNodeDetails;
      } catch (CompletionException | CancellationException e) {
         LOGGER.error("Error while getting cluster info", e);
         this.metrics.increment(MetricName.GET_CLUSTER_INFO, Tag.statusError());
         throw new KafkaCommunicationException(e);
      }
   }

   @Override
   @Timed(value = "kafka_client_get_nodes_time")
   public List<KafkaNodeDetails> getNodes() {
      LOGGER.info("[flow:nodes.get] Getting nodes from Kafka cluster");
      this.metrics.increment(MetricName.LIST_CLUSTER_NODE, Tag.statusInit());

      try (AdminClient adminClient = AdminClient.create(this.props)) {
         CompletableFuture<Set<String>> topicNamesFuture = adminClient.listTopics().names().toCompletionStage()
               .toCompletableFuture();

         CompletableFuture<Collection<Node>> nodesFuture = adminClient.describeCluster().nodes().toCompletionStage()
               .toCompletableFuture();

         CompletableFuture.allOf(topicNamesFuture, nodesFuture).join();

         Set<String> names = topicNamesFuture.join();

         LOGGER.info("[flow:nodes.get] Getting nodes, topic names size is [{}]", names.size());

         Collection<Node> nodes = nodesFuture.join();

         LOGGER.info("[flow:nodes.get] Getting nodes, nodes size is [{}]", nodes.size());

         if (nodes.isEmpty()) {
            LOGGER.info("[flow:nodes.get] There is no nodes on Kafka cluster");
            this.metrics.increment(MetricName.LIST_CLUSTER_NODE, Tag.statusSuccess());
            return List.of();
         }

         Map<String, TopicDescription> uuidTopicDescriptionMap = new HashMap<>();
         if (!names.isEmpty()) {
            CompletableFuture<Map<String, TopicDescription>> completableFuture = adminClient.describeTopics(names).all()
                  .toCompletionStage().toCompletableFuture();
            uuidTopicDescriptionMap = completableFuture.join();
         }

         Map<Integer, Integer> counter = new HashMap<>();
         if (!Objects.isNull(uuidTopicDescriptionMap)) {
            for (TopicDescription topicDescription : uuidTopicDescriptionMap.values()) {
               topicDescription.partitions().forEach(partition -> {
                  int leaderId = partition.leader().id();
                  counter.merge(leaderId, 1, Integer::sum);
               });
            }
         }

         List<KafkaNodeDetails> list = nodes.stream().map(node -> {
            Integer topics = counter.getOrDefault(node.id(), 0);
            return new KafkaNodeDetails(node.idString(), node.host(), node.hasRack(), node.rack(), node.port(),
                  node.isEmpty(), topics);
         }).toList();
         this.metrics.increment(MetricName.LIST_CLUSTER_NODE, Tag.statusSuccess());
         return list;
      } catch (CompletionException | CancellationException e) {
         LOGGER.error("Error while getting nodes from Kafka cluster", e);
         this.metrics.increment(MetricName.LIST_CLUSTER_NODE, Tag.statusError());
         throw new KafkaCommunicationException(e);
      }
   }

   @Override
   @Timed(value = "kafka_client_get_topic_by_id_time")
   public Optional<TopicDescription> getTopicById(String id) {
      LOGGER.info("[flow:topic.get] Getting topic by id, using the id '{}'", id);
      this.metrics.increment(MetricName.GET_TOPIC_BY_ID, Tag.statusInit());
      Uuid uuid = Uuid.fromString(id);

      try (AdminClient adminClient = AdminClient.create(this.props)) {
         Map<Uuid, TopicDescription> response = adminClient.describeTopics(TopicCollection.ofTopicIds(List.of(uuid)))
               .allTopicIds().toCompletionStage().toCompletableFuture().join();
         Optional<TopicDescription> possibleTopic = response.values().stream().findFirst();
         this.metrics.increment(MetricName.GET_TOPIC_BY_ID, Tag.statusSuccess());
         return possibleTopic;
      } catch (UnknownTopicIdException | CompletionException | CancellationException e) {
         if (!(e.getCause() instanceof UnknownTopicIdException)) {
            LOGGER.error("Error while getting topic by id, using the id '%s'".formatted(id), e);
            this.metrics.increment(MetricName.GET_TOPIC_BY_ID, Tag.statusError());
            throw new KafkaCommunicationException(e);
         }
         this.metrics.increment(MetricName.GET_TOPIC_BY_ID, Tag.statusSuccess());
         throw e;
      }
   }
}
