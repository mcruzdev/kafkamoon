package dev.matheuscruz.kafkamoon.api.application.usecases.topics.create;

import static org.hamcrest.MatcherAssert.assertThat;

import dev.matheuscruz.kafkamoon.api.application.model.exception.InvalidCriticalityException;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTopicUseCaseTest {

  @Mock KafkaClient kafkaClient;

  @InjectMocks CreateTopicUseCase sut;

  @Test
  @DisplayName("Should throw invalid criticality exception when the criticality name is incorrect")
  void shouldThrowInvalidCriticalityException() {

    // arrange
    CreateTopicUseCaseInput input =
        new CreateTopicUseCaseInput("user", "payments", "payment-received", "error");

    // act
    InvalidCriticalityException exception =
        Assertions.assertThrows(
            InvalidCriticalityException.class,
            () -> {
              sut.execute(input);
            });

    String message = exception.getMessage();

    assertThat(message, Matchers.containsString("Invalid criticality name, allowed values are"));

    // should not call kakfaClient
    Mockito.verify(kafkaClient, Mockito.times(0)).getTopicById(Mockito.anyString());
  }

  @Test
  @DisplayName("Should create a topic correctly")
  void shouldCreateATopicCorrectly() {
    // arrange
    CreateTopicUseCaseInput input =
        new CreateTopicUseCaseInput("user", "payments", "payment-received", "test");

    ArgumentCaptor<String> topicNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> partitionCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Short> replicationFactorCaptor = ArgumentCaptor.forClass(Short.class);
    Mockito.when(kafkaClient.createTopic(Mockito.any(), Mockito.anyInt(), Mockito.anyShort()))
        .thenReturn("user.payments.payment-received");

    // act
    CreateTopicUseCaseOutput output = sut.execute(input);

    Mockito.verify(kafkaClient)
        .createTopic(
            topicNameCaptor.capture(),
            partitionCaptor.capture(),
            replicationFactorCaptor.capture());

    String expectedTopicName = "user.payments.payment-received";
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(output.id()).isNotBlank();
          softly.assertThat(output.name()).isEqualTo(expectedTopicName);
          softly.assertThat(topicNameCaptor.getValue()).isEqualTo(expectedTopicName);
          softly.assertThat(partitionCaptor.getValue()).isEqualTo(1);
          softly.assertThat(replicationFactorCaptor.getValue()).isEqualTo((short) 1);
        });
  }
}
