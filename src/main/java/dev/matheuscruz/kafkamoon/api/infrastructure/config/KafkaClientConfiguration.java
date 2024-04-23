package dev.matheuscruz.kafkamoon.api.infrastructure.config;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.DefaultKafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaClientConfiguration {

   private static final Logger LOGGER = LoggerFactory.getLogger(KafkaClientConfiguration.class);

   @Value("${kafkamoon.kafka.bootstrap.servers}")
   private String bootstrapServers;

   @Bean
   public KafkaClient kafkaClient() {
      LOGGER.info("[flow:startup.config] Createing KafkaClient bean with bootstrap.servers as {}", bootstrapServers);
      Properties props = new Properties();
      props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      return new DefaultKafkaClient(props);
   }
}
