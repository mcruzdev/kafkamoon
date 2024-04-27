package dev.matheuscruz.kafkamoon.api.model.topic;

import java.util.Objects;

/**
 * Value object class that represents a domain topic name.
 */
public final class TopicName {

  private static final int MAX_TOPIC_NAME_LENGTH = 255;
  private final String messageType;
  private final String dataset;
  private final String dataName;
  private final String name;

  /**
   * @param messageType {@link String} representing the message type.
   * @param dataset     {@link String} representing the dataset.
   * @param dataName    {@link String} representing the data name.
   */
  public TopicName(String messageType, String dataset, String dataName) {
    this.messageType = messageType;
    this.dataset = dataset;
    this.dataName = dataName;
    String finalName = "%s.%s.%s".formatted(messageType, dataset, dataName);
    if (finalName.length() > MAX_TOPIC_NAME_LENGTH) {
      throw new TopicNameExceededException(finalName);
    }
    this.name = finalName;
  }

  public String finalName() {
    return this.name;
  }

  public String messageType() {
    return messageType;
  }

  public String dataset() {
    return dataset;
  }

  public String dataName() {
    return dataName;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (TopicName) obj;
    return Objects.equals(this.messageType, that.messageType)
        && Objects.equals(this.dataset, that.dataset)
        && Objects.equals(this.dataName, that.dataName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageType, dataset, dataName);
  }

  @Override
  public String toString() {
    return "TopicName["
        + "messageType="
        + messageType
        + ", "
        + "dataset="
        + dataset
        + ", "
        + "dataName="
        + dataName
        + ']';
  }
}
