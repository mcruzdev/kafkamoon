package dev.matheuscruz.kafkamoon.api.presentation.controller;

import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaClient;
import dev.matheuscruz.kafkamoon.api.infrastructure.security.SecurityConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

class ClusterControllerITest extends AbstractBaseITest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClusterControllerITest.class);

  @Autowired MockMvc mockMvc;

  @Autowired KafkaClient kafkaClient;

  @Container
  static KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

  @DynamicPropertySource
  static void registryConfig(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add(
        "kafkamoon.kafka.bootstrap.servers",
        () -> {
          LOGGER.info(
              "[profile:it] Using kafkamoon.kafka.bootstrap.servers={}",
              kafka.getBootstrapServers());
          return kafka.getBootstrapServers();
        });
  }

  @Test
  @DisplayName("Should get cluster info correctly")
  @WithMockUser(roles = SecurityConfiguration.ROLE_READER)
  void shouldGetClusterInfo() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/cluster/info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
  }

  @Test
  @DisplayName("Should get cluster node details")
  @WithMockUser(roles = SecurityConfiguration.ROLE_READER)
  void shouldGetClusterDetails() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/cluster/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(Matchers.is("1")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].host").value("localhost"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantityOfTopics").value(Matchers.is(0)));
  }

  @Test
  @DisplayName("Should get cluster node details with quantityOfTopics equals to 1")
  @WithMockUser(roles = SecurityConfiguration.ROLE_READER)
  void shouldGetClusterDetailsWithTopic() throws Exception {
    // arrange
    String topicId = kafkaClient.createTopic("user.payments.payment-created", 1, (short) 1);

    // act, assert
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/cluster/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(Matchers.is("1")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].host").value("localhost"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantityOfTopics").value(Matchers.is(1)));

    // delete topic
    kafkaClient.deleteTopic(topicId);
  }
}
