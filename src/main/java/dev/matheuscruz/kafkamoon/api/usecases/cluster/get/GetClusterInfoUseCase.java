package dev.matheuscruz.kafkamoon.api.usecases.cluster.get;

import dev.matheuscruz.kafkamoon.api.domain.cluster.KafkaClusterInfo;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import org.springframework.stereotype.Component;

@Component
public class GetClusterInfoUseCase {

  private final KafkaClient kafkaClient;

  public GetClusterInfoUseCase(KafkaClient kafkaClient) {
    this.kafkaClient = kafkaClient;
  }

  public GetClusterInfoUseCaseOutput execute() {
    KafkaClusterInfo clusterInfo = this.kafkaClient.getClusterInfo();
    return new GetClusterInfoUseCaseOutput(
        clusterInfo.id(),
        clusterInfo.host(),
        clusterInfo.hasRack(),
        clusterInfo.rack(),
        clusterInfo.port(),
        clusterInfo.isEmpty());
  }
}
