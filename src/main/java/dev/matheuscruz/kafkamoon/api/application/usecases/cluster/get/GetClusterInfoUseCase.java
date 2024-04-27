package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.get;

import dev.matheuscruz.kafkamoon.api.application.model.cluster.KafkaNodeDetails;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import org.springframework.stereotype.Component;

@Component
public class GetClusterInfoUseCase {

  private final KafkaClient kafkaClient;
  private final Metrics metrics;

  public GetClusterInfoUseCase(KafkaClient kafkaClient, Metrics metrics) {
    this.kafkaClient = kafkaClient;
    this.metrics = metrics;
  }

  public GetClusterInfoUseCaseOutput execute() {
    this.metrics.increment(MetricName.GET_CLUSTER_INFO, Tag.statusInit());
    KafkaNodeDetails clusterInfo = this.kafkaClient.getClusterInfo();
    GetClusterInfoUseCaseOutput output =
        new GetClusterInfoUseCaseOutput(
            clusterInfo.id(),
            clusterInfo.host(),
            clusterInfo.hasRack(),
            clusterInfo.rack(),
            clusterInfo.port(),
            clusterInfo.isEmpty());
    this.metrics.increment(MetricName.GET_CLUSTER_INFO, Tag.statusSuccess());
    return output;
  }
}
