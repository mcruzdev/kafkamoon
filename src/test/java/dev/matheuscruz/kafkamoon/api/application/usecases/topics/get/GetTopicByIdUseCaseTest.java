package dev.matheuscruz.kafkamoon.api.application.usecases.topics.get;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import java.util.Optional;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.errors.UnknownTopicIdException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTopicByIdUseCaseTest {

  @Mock private KafkaClient kafkaClient;

  @InjectMocks private GetTopicByIdUseCase sut;

  @Test
  @DisplayName("Should throws UnknownTopicIdException when there is no topic with the provided id")
  void shouldNotReturnATopic() {
    // arrange
    String uuid = Uuid.randomUuid().toString();
    Mockito.when(kafkaClient.getTopicById(uuid)).thenReturn(Optional.empty());

    // arrange, assert
    UnknownTopicIdException exception =
        assertThrows(
            UnknownTopicIdException.class,
            () -> {
              GetTopicByIdUseCaseOutput output = sut.execute(uuid);
            });

    assertThat(
        exception.getMessage(),
        Matchers.containsString("Topic with id %s does not exist".formatted(uuid)));
  }
}
