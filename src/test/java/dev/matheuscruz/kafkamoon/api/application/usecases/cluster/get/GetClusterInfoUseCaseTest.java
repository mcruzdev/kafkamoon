package dev.matheuscruz.kafkamoon.api.application.usecases.cluster.get;

import dev.matheuscruz.kafkamoon.api.application.model.cluster.KafkaNodeDetails;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetClusterInfoUseCaseTest {

  @Mock KafkaClient kafkaClient;

  @Mock Metrics metrics;

  @InjectMocks GetClusterInfoUseCase sut;

  @Test
  void shouldGetClusterInfoCorrectly() {
    // arrange
    Mockito.when(kafkaClient.getClusterInfo())
        .thenReturn(new KafkaNodeDetails("1", "localhost", false, null, 9092, false, null));

    // act
    GetClusterInfoUseCaseOutput output = sut.execute();

    // assert
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(output.id()).isEqualTo("1");
          softly.assertThat(output.rack()).isEqualTo(null);
          softly.assertThat(output.hasRack()).isEqualTo(false);
          softly.assertThat(output.isEmpty()).isEqualTo(false);
          softly.assertThat(output.host()).isEqualTo("localhost");
          softly.assertThat(output.port()).isEqualTo(9092);
        });

    Mockito.verify(kafkaClient, Mockito.times(1)).getClusterInfo();
  }
}
