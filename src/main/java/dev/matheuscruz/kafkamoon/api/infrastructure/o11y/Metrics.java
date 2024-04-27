package dev.matheuscruz.kafkamoon.api.infrastructure.o11y;

public interface Metrics {

  void increment(MetricName metric, Tag... tags);
}
