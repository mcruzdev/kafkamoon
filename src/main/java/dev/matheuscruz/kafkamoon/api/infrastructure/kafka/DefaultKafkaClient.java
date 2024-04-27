package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaNodeDetails;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

  public DefaultKafkaClient(final Properties props) {
    this.props = props;
  }

  public String createTopic(String topicName, Integer partitions, Short replicationFactor) {
    LOGGER.info("[flow:create.topic] Creating topic with name {}", topicName);
    try (AdminClient adminClient = AdminClient.create(this.props)) {
      CreateTopicsResult result =
          adminClient.createTopics(
              List.of(
                  new NewTopic(
                      topicName,
                      Optional.ofNullable(partitions),
                      Optional.ofNullable(replicationFactor))));
      return result.topicId(topicName).get().toString();
    } catch (ExecutionException | InterruptedException e) {
      if (e.getCause() != null && e.getCause() instanceof TopicExistsException) {
        throw new TopicExistsException("Topic with name %s already exists".formatted(topicName), e);
      }
      throw new RuntimeException(e);
    }
  }

  public Collection<TopicListing> listTopics() {
    LOGGER.info("[flow:list.topic] Listing topics from Kafka bootstrap servers");
    try (AdminClient adminClient = AdminClient.create(this.props)) {
      ListTopicsResult result = adminClient.listTopics();
      return result.listings().get();
    } catch (ExecutionException | InterruptedException e) {
      LOGGER.error("Was not possible to list topics from Kafka cluster", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteTopic(String topicId) {
    try (AdminClient adminClient = AdminClient.create(this.props)) {
      DeleteTopicsResult result =
          adminClient.deleteTopics(TopicCollection.ofTopicIds(List.of(Uuid.fromString(topicId))));
      result.all().get();
    } catch (ExecutionException | InterruptedException e) {
      if (e.getCause() != null && e.getCause() instanceof UnknownTopicIdException) {
        LOGGER.warn("There is no topic with id {} on Kafka cluster", topicId);
        throw new UnknownTopicIdException(e.getMessage());
      } else {
        LOGGER.error("Was not possible to delete topic with id %s".formatted(topicId), e);
      }
    }
  }

  @Override
  public KafkaNodeDetails getClusterInfo() {
    try (AdminClient adminClient = AdminClient.create(this.props)) {
      DescribeClusterResult result = adminClient.describeCluster();
      Node kafkaNode = result.controller().get();
      return new KafkaNodeDetails(
          kafkaNode.idString(),
          kafkaNode.host(),
          kafkaNode.hasRack(),
          kafkaNode.rack(),
          kafkaNode.port(),
          kafkaNode.isEmpty(),
          null);
    } catch (ExecutionException | InterruptedException e) {
      // TODO: throw a meaningful Exception after
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<KafkaNodeDetails> getNodes() {

    try (AdminClient adminClient = AdminClient.create(this.props)) {
      CompletableFuture<Set<String>> topicNamesFuture =
          adminClient.listTopics().names().toCompletionStage().toCompletableFuture();

      CompletableFuture<Collection<Node>> nodesFuture =
          adminClient.describeCluster().nodes().toCompletionStage().toCompletableFuture();

      CompletableFuture.allOf(topicNamesFuture, nodesFuture).join();

      Set<String> names = topicNamesFuture.join();

      LOGGER.info("[flow:nodes.get] Getting nodes, topic names size is [{}]", names.size());

      Collection<Node> nodes = nodesFuture.join();

      LOGGER.info("[flow:nodes.get] Getting nodes, nodes size is [{}]", nodes.size());

      if (nodes.isEmpty()) {
        return List.of();
      }

      Map<String, TopicDescription> uuidTopicDescriptionMap = new HashMap<>();
      if (!names.isEmpty()) {
        CompletableFuture<Map<String, TopicDescription>> completableFuture =
            adminClient.describeTopics(names).all().toCompletionStage().toCompletableFuture();
        uuidTopicDescriptionMap = completableFuture.join();
      }

      Map<Integer, Integer> counter = new HashMap<>();
      if (!Objects.isNull(uuidTopicDescriptionMap)) {
        for (TopicDescription topicDescription : uuidTopicDescriptionMap.values()) {
          topicDescription
              .partitions()
              .forEach(
                  partition -> {
                    int leaderId = partition.leader().id();
                    counter.merge(leaderId, 1, Integer::sum);
                  });
        }
      }

      return nodes.stream()
          .map(
              node -> {
                Integer topics = counter.getOrDefault(node.id(), 0);
                return new KafkaNodeDetails(
                    node.idString(),
                    node.host(),
                    node.hasRack(),
                    node.rack(),
                    node.port(),
                    node.isEmpty(),
                    topics);
              })
          .toList();
    }
  }

  @Override
  public Optional<TopicDescription> getTopicByName(String name) {
    try (AdminClient adminClient = AdminClient.create(this.props)) {
      Map<Uuid, TopicDescription> response =
          adminClient
              .describeTopics(TopicCollection.ofTopicIds(List.of(Uuid.fromString(name))))
              .allTopicIds()
              .toCompletionStage()
              .toCompletableFuture()
              .join();
      return response.values().stream().findFirst();
    }
  }
}
