package dev.matheuscruz.kafkamoon.api.application.model.topic;

import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TopicNameTest {

  @Test
  @DisplayName("Should create the topic name correctly")
  void shouldCreateCorrectly() {

    // act
    TopicName sut = new TopicName("user", "hi", "high");

    // assert
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(sut).isNotNull();
          softly.assertThat(sut.finalName()).isEqualTo("user.hi.high");
        });
  }

  @Test
  @DisplayName("Should throw TopicNameExceededException when the topic name is large")
  void shouldThrowsTopicNameExceededException() {
    // arrange
    String messageType = RandomStringUtils.random(255);

    // act
    TopicNameExceededException exception =
        Assertions.assertThrows(
            TopicNameExceededException.class,
            () -> {
              TopicName sut = new TopicName(messageType, "dataset", "dataName");
            });

    assertThat(
        exception.getMessage(), Matchers.containsString("exceeds the maximum allowed length."));
  }
}
