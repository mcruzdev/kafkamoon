package dev.matheuscruz.kafkamoon.api.infrastructure.o11y;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TagTest {

  @Test
  @DisplayName("Should create a Tag correctly")
  void shouldCreateTagCorrectly() {
    // arrange
    Tag tag = Tag.create("hello", "world");

    // act
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(tag.selfValidate()).isTrue();
          softly.assertThat(tag.key()).isEqualTo("hello");
          softly.assertThat(tag.value()).isEqualTo("world");
        });
  }

  @ParameterizedTest
  @DisplayName("Should validate a Tag correctly")
  @CsvSource(
      value = {";world", "hello;", ";"},
      delimiter = ';')
  void shouldReturnFalseWhenTagIsInvalid(String key, String value) {
    // arrange, act
    Tag tag = Tag.create(key, value);

    // assert
    Assertions.assertFalse(tag.selfValidate());
  }
}
