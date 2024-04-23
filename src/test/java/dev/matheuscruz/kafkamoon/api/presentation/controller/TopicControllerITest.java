package dev.matheuscruz.kafkamoon.api.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuscruz.kafkamoon.api.Application;
import dev.matheuscruz.kafkamoon.api.domain.topics.TopicCriticality;
import dev.matheuscruz.kafkamoon.api.presentation.dto.CreateTopicRequest;
import dev.matheuscruz.kafkamoon.api.usecases.topics.list.ListTopicsUseCaseOutput;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = { "classpath:application-it.yaml" })
@Testcontainers
class TopicControllerITest {

   private static final Logger LOGGER = LoggerFactory.getLogger(TopicControllerITest.class);

   @Container
   static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

   @DynamicPropertySource
   static void registryConfig(DynamicPropertyRegistry dynamicPropertyRegistry) {
      dynamicPropertyRegistry.add("kafkamoon.kafka.bootstrap.servers", () -> {
         LOGGER.info("[env:it] Using kafkamoon.kafka.bootstrap.servers={}", kafka.getBootstrapServers());
         return kafka.getBootstrapServers();
      });
   }

   @Autowired
   MockMvc mockMvc;

   @Autowired
   ObjectMapper mapper;

   @Test
   @DisplayName("Should create a topic correctly when the request is valid")
   void shouldReturn201Created() throws Exception {
      // arrange
      CreateTopicRequest request = new CreateTopicRequest("queuing", "database", "table", TopicCriticality.TEST.name());
      String requestBody = mapper.writeValueAsString(request);

      // act, assert
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON).content(requestBody)
            .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated());
   }

   @Test
   @DisplayName("Should not create a topic with the same name")
   void shouldReturn409Conflict() throws Exception {

      // arrange
      CreateTopicRequest request = new CreateTopicRequest("logging", "database", "table", TopicCriticality.TEST.name());
      String requestBody = mapper.writeValueAsString(request);

      // first time
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON).content(requestBody)
            .accept(MediaType.APPLICATION_JSON));

      // act, assert
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                  .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(jsonPath("$.type", Matchers.containsString("topic-with-conflict")))
            .andExpect(jsonPath("$.title", Matchers.is("Conflict")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(409)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("logging.database.table already exists.")));
   }

   @Test
   @DisplayName("Should not create a topic when message type is null")
   void shouldReturn400BadRequestMessageTypeIsNull() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest(null, "dataset", "dataName", "TEST");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when message type is empty")
   void shouldReturn400BadRequestMessageTypeIsEmpty() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("", "dataset", "dataName", "TEST");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when data name is null")
   void shouldReturn400BadRequestDataNameIsNull() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("user", "dataset", null, "TEST");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when data name is empty")
   void shouldReturn400BadRequestDataNameIsEmpty() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("user", "dataset", "", "TEST");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when dataset is null")
   void shouldReturn400BadRequestDatasetIsNull() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("user", null, "dataName", "TEST");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when dataset is empty")
   void shouldReturn400BadRequestDatasetIsEmpty() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("user", "", "dataName", "TEST");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when dataset is empty")
   void shouldReturn400BadRequestCriticalityIsEmpty() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("user", "dataset", "dataName", "");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when dataset is empty")
   void shouldReturn400BadRequestCriticalityIsNotTest() throws Exception {

      CreateTopicRequest request = new CreateTopicRequest("user", "dataset", "dataName", "PRODUCTION");
      String requestBody = mapper.writeValueAsString(request);
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("invalid-request")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("Invalid request content")));

   }

   @Test
   @DisplayName("Should not create a topic when the topic name exceeds the length")
   void shouldReturn400BadRequestTopicNameExceeded() throws Exception {
      // arrange
      CreateTopicRequest request = new CreateTopicRequest("queuing", "database",
            "aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890",
            TopicCriticality.TEST.name());
      String requestBody = mapper.writeValueAsString(request);

      // act, assert
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", Matchers.containsString("topic-name-exceeded-limit")))
            .andExpect(jsonPath("$.title", Matchers.is("Bad Request")))
            .andExpect(jsonPath("$.instance", Matchers.is("/api/v1/topics")))
            .andExpect(jsonPath("$.status", Matchers.is(400)))
            .andExpect(jsonPath("$.detail", Matchers.containsString("exceeds the maximum allowed length.")));
   }

   @Test
   @DisplayName("Should list topics correctly when page is 0 and size is 0")
   void shouldReturnEmptyPage() throws Exception {

      // arrange
      CreateTopicRequest request = new CreateTopicRequest("user", "payments", "payment-created",
            TopicCriticality.TEST.name());
      String requestBody = mapper.writeValueAsString(request);

      // act
      mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON).content(requestBody)
            .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated());

      String responseBody = mockMvc.perform(
                  get("/api/v1/topics").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

      ListTopicsUseCaseOutput[] arr = mapper.readValue(responseBody, ListTopicsUseCaseOutput[].class);

      // assert
      SoftAssertions.assertSoftly(softly -> {
         softly.assertThat(arr).isNotEmpty();
         softly.assertThat(arr).anyMatch(topic -> topic.name().equals("user.payments.payment-created"));
      });
   }
}
