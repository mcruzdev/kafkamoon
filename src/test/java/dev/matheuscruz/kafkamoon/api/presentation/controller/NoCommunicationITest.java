package dev.matheuscruz.kafkamoon.api.presentation.controller;

import dev.matheuscruz.kafkamoon.api.Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application-it.yaml"})
public class NoCommunicationITest {

  @Autowired MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "READER")
  @DisplayName("Should return bad gateway when there is no communication with Kafka")
  void shouldReturnBadGateway() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/cluster/info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadGateway());
  }
}
