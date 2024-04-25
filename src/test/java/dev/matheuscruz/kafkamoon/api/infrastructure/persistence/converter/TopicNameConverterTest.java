package dev.matheuscruz.kafkamoon.api.infrastructure.persistence.converter;

import static org.hamcrest.MatcherAssert.assertThat;

import dev.matheuscruz.kafkamoon.api.domain.topics.TopicName;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TopicNameConverterTest {

  @Test
  @DisplayName("Should convert value object to database column correctly")
  void shouldConvertToDatabaseColumn() {
    // arrange
    TopicNameConverter sut = new TopicNameConverter();

    // act
    String databaseColumn = sut.convertToDatabaseColumn(new TopicName("a", "hasRack", "c"));

    // assert
    assertThat(databaseColumn, Matchers.is("a.hasRack.c"));
  }

  @Test
  @DisplayName("Should convert database column to value object correctly")
  void shouldConvertToTopicName() {
    // arrange
    TopicNameConverter topicNameConverter = new TopicNameConverter();

    // act
    TopicName topicName = topicNameConverter.convertToEntityAttribute("a.hasRack.c");

    // assert
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(topicName.messageType()).isEqualTo("a");
          softly.assertThat(topicName.dataset()).isEqualTo("hasRack");
          softly.assertThat(topicName.dataName()).isEqualTo("c");
          softly.assertThat(topicName.finalName()).isEqualTo("a.hasRack.c");
        });
  }
}
