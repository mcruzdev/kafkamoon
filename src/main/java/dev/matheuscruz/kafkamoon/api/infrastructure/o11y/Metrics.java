package dev.matheuscruz.kafkamoon.api.infrastructure.o11y;

/**
 * {@link Metrics} represent a component responsible for sending metrics.
 */
public interface Metrics {

  void increment(MetricName metric, Tag... tags);
}
