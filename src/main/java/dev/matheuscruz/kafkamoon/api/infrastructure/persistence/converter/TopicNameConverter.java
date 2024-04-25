package dev.matheuscruz.kafkamoon.api.infrastructure.persistence.converter;

import dev.matheuscruz.kafkamoon.api.domain.topics.TopicName;
import jakarta.persistence.AttributeConverter;

/**
 * TopicNameConverter aims to convert database column to value object and value object to database column.
 */
public class TopicNameConverter implements AttributeConverter<TopicName, String> {
  @Override
  public String convertToDatabaseColumn(TopicName attribute) {
    return attribute.finalName();
  }

  @Override
  public TopicName convertToEntityAttribute(String dbData) {
    String[] arr = dbData.split("\\.");
    final int messageType = 0;
    final int dataName = 1;
    final int dataset = 2;
    return new TopicName(arr[messageType], arr[dataName], arr[dataset]);
  }
}
