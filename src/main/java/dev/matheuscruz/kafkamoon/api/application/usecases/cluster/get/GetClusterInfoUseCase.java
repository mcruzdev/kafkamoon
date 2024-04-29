package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.get;

import dev.matheuscruz.kafkamoon.api.application.model.cluster.KafkaNodeDetails;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import org.springframework.stereotype.Component;

/**
 * {@link GetClusterInfoUseCase} represents a use case. This use case gets the info about the Kafka cluster.
 */
@Component
public class GetClusterInfoUseCase {

  private final KafkaClient kafkaClient;

  public GetClusterInfoUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public GetClusterInfoUseCaseOutput execute() {
    KafkaNodeDetails clusterInfo = this.kafkaClient.getClusterInfo();
    return new GetClusterInfoUseCaseOutput(
        clusterInfo.id(),
        clusterInfo.host(),
        clusterInfo.hasRack(),
        clusterInfo.rack(),
        clusterInfo.port(),
        clusterInfo.isEmpty());
  }
}
