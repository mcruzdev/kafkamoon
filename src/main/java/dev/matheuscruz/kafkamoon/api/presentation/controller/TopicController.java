package dev.matheuscruz.kafkamoon.api.presentation.controller;

import dev.matheuscruz.kafkamoon.api.presentation.dto.CreateTopicRequest;
import dev.matheuscruz.kafkamoon.api.usecases.topics.create.CreateTopicUseCase;
import dev.matheuscruz.kafkamoon.api.usecases.topics.create.CreateTopicUseCaseInput;
import dev.matheuscruz.kafkamoon.api.usecases.topics.create.CreateTopicUseCaseOutput;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/topics")
@RestController
public class TopicController {

   private static final Logger LOGGER = LoggerFactory.getLogger(TopicController.class);

   private final CreateTopicUseCase createTopicUseCase;

   public TopicController(CreateTopicUseCase createTopicUseCase) {
      this.createTopicUseCase = createTopicUseCase;
   }

   @PostMapping
   public ResponseEntity<CreateTopicUseCaseOutput> create(@Valid @RequestBody CreateTopicRequest request) {

      CreateTopicUseCaseOutput output = createTopicUseCase.execute(
            new CreateTopicUseCaseInput(request.messageType(), request.dataset(), request.dataName(),
                  request.criticality()));
      String location = "/api/v1/topics/%s".formatted(output.topicId());
      return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).body(output);
   }
}
