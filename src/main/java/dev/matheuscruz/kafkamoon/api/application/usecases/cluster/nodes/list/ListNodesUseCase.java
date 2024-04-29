package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.nodes.list;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListNodesUseCase {

  private final KafkaClient kafkaClient;

  public ListNodesUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public List<ListNodesUseCaseOutput> execute() {
    return this.kafkaClient.getNodes().stream()
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
  }
}
