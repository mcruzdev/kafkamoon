package dev.matheuscruz.kafkamoon.api.presentation.controller;

import dev.matheuscruz.kafkamoon.api.presentation.dto.CreateTopicRequest;
import dev.matheuscruz.kafkamoon.api.usecases.topics.create.CreateTopicUseCase;
import dev.matheuscruz.kafkamoon.api.usecases.topics.create.CreateTopicUseCaseInput;
import dev.matheuscruz.kafkamoon.api.usecases.topics.create.CreateTopicUseCaseOutput;
import dev.matheuscruz.kafkamoon.api.usecases.topics.list.ListTopicsUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/topics")
@RestController
public class TopicController {

   private static final Logger LOGGER = LoggerFactory.getLogger(TopicController.class);

   private final CreateTopicUseCase createTopicUseCase;
   private final ListTopicsUseCase listTopicsUseCase;

   public TopicController(CreateTopicUseCase createTopicUseCase, ListTopicsUseCase listTopicsUseCase) {
      this.createTopicUseCase = createTopicUseCase;
      this.listTopicsUseCase = listTopicsUseCase;
   }

   @PostMapping
   public ResponseEntity<CreateTopicUseCaseOutput> create(@Valid @RequestBody CreateTopicRequest request) {
      LOGGER.info("[flow:create.topic] Receiving HTTP request to create a topic: {}", request);
      CreateTopicUseCaseOutput output = createTopicUseCase.execute(
            new CreateTopicUseCaseInput(request.messageType(), request.dataset(), request.dataName(),
                  request.criticality()));
      String location = "/api/v1/topics/%s".formatted(output.topicId());
      return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).body(output);
   }

   @GetMapping
   public ResponseEntity<?> index() {
      LOGGER.info("[flow:list.topic] Receiving HTTP request to list topics");
      return ResponseEntity.ok(this.listTopicsUseCase.execute());
   }
}
