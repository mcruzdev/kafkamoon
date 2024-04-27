package dev.matheuscruz.kafkamoon.api.infrastructure.o11y;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeterRegistryConfig {

  @Value("${spring.application.name}")
  private String application;

  @Bean
  MeterRegistryCustomizer<MeterRegistry> commonTags() {
    return registry -> registry.config().commonTags("application", application);
  }
}
