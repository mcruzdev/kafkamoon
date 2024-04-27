package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.nodes.list;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListNodesUseCase {

  private final KafkaClient kafkaClient;
  private final Metrics metrics;

  public ListNodesUseCase(KafkaClient kafkaClient, Metrics metrics) {
    this.kafkaClient = kafkaClient;
    this.metrics = metrics;
  }

  public List<ListNodesUseCaseOutput> execute() {
    this.metrics.increment(MetricName.LIST_CLUSTER_NODE, Tag.statusInit());
    List<ListNodesUseCaseOutput> output =
        this.kafkaClient.getNodes().stream()
            .map(
                node ->
                    new ListNodesUseCaseOutput(
                        node.id(),
                        node.host(),
                        node.port(),
                        node.hasRack(),
                        node.rack(),
                        node.isEmpty(),
                        node.quantityOfTopics()))
            .toList();
    this.metrics.increment(MetricName.LIST_CLUSTER_NODE, Tag.statusSuccess());
    return output;
  }
}
