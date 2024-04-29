package dev.matheuscruz.kafkamoon.api.application.model.topic;

import static org.hamcrest.MatcherAssert.assertThat;

import dev.matheuscruz.kafkamoon.api.application.model.exception.InvalidCriticalityException;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TopicCriticalityTest {

  @Test
  @DisplayName("Should create Criticality#TEST correctly")
  void testCriticalityShouldHaveOnePartitionsAndReplicationFactorOne() {
    // arrange, act
    TopicCriticality sut = TopicCriticality.TEST;

    // assert
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(sut.getPartitions()).isEqualTo(1);
          softly.assertThat(sut.getReplicationFactor()).isEqualTo((short) 1);
        });
  }

  @Test
  void shouldThrowsExceptionOnWrongValue() {
    // arrange, act
    InvalidCriticalityException exception =
        Assertions.assertThrows(
            InvalidCriticalityException.class,
            () -> {
              TopicCriticality.fromString("WRONG");
            });

    // assert
    assertThat(
        exception.getMessage(),
        Matchers.containsString("Invalid criticality name, allowed values are: "));
  }
}
