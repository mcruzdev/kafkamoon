package dev.matheuscruz.kafkamoon.api.infrastructure.o11y;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MeterMetric implements Metrics {

  private final String applicationName;
  private final MeterRegistry meterRegistry;

  public MeterMetric(
      @Value("${spring.application.name}") String applicationName, MeterRegistry meterRegistry) {
    this.applicationName = applicationName;
    this.meterRegistry = meterRegistry;
  }

  @Override
  public void increment(MetricName metric, Tag... tags) {
    Counter.Builder builder = Counter.builder(buildName(metric));
    for (Tag tag : tags) {
      builder.tag(tag.key(), tag.value());
    }
    builder.register(meterRegistry).increment();
  }

  private String buildName(MetricName metric) {
    return "%s.%s".formatted(this.applicationName, metric.name());
  }
}
