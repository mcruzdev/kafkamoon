package dev.matheuscruz.kafkamoon.api.infrastructure.kafka;

import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaClientConfiguration {

   private static final Logger LOGGER = LoggerFactory.getLogger(KafkaClientConfiguration.class);

   @Value("${kafkamoon.kafka.bootstrap.servers}")
   private String bootstrapServers;

   @Value("${kafkamoon.kafka.retries}")
   private Integer kafkaClientRetries;

   @Value("${kafkamoon.kafka.default.api.timeout.ms}")
   private Integer kafkaDefaultApiTimeoutMs;

   @Value("${spring.application.name}")
   private String applicationName;


   @Bean
   public KafkaClient kafkaClient() {
      LOGGER.info("[flow:startup.config] Creating KafkaClient bean with bootstrap.servers as {}", bootstrapServers);
      Properties props = new Properties();
      props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      props.put("retries", kafkaClientRetries);
      props.put("client.id", applicationName);
      props.put("default.api.timeout.ms", kafkaDefaultApiTimeoutMs);
      props.put("request.timeout.ms", kafkaDefaultApiTimeoutMs);
      return new DefaultKafkaClient(props);
   }
}
