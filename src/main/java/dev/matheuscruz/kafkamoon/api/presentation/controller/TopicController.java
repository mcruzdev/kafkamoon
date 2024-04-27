package dev.matheuscruz.kafkamoon.api.presentation.controller;

import dev.matheuscruz.kafkamoon.api.application.usecases.topics.create.CreateTopicUseCase;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.create.CreateTopicUseCaseInput;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.create.CreateTopicUseCaseOutput;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.delete.DeleteTopicUseCase;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.get.GetTopicByIdUseCase;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.get.GetTopicByNameUseCaseOutput;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.list.ListTopicsUseCase;
import dev.matheuscruz.kafkamoon.api.application.usecases.topics.list.ListTopicsUseCaseOutput;
import dev.matheuscruz.kafkamoon.api.presentation.dto.CreateTopicRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final DeleteTopicUseCase deleteTopicUseCase;
  private final GetTopicByIdUseCase getTopicByIdUseCase;

  public TopicController(
      CreateTopicUseCase createTopicUseCase,
      ListTopicsUseCase listTopicsUseCase,
      DeleteTopicUseCase deleteTopicUseCase,
      GetTopicByIdUseCase getTopicByIdUseCase) {
    this.createTopicUseCase = createTopicUseCase;
    this.listTopicsUseCase = listTopicsUseCase;
    this.deleteTopicUseCase = deleteTopicUseCase;
    this.getTopicByIdUseCase = getTopicByIdUseCase;
  }

  @PostMapping
  public ResponseEntity<CreateTopicUseCaseOutput> create(
      @Valid @RequestBody CreateTopicRequest request) {
    LOGGER.info("[flow:create.topic] Receiving HTTP request to create a topic: {}", request);
    CreateTopicUseCaseOutput output =
        createTopicUseCase.execute(
            new CreateTopicUseCaseInput(
                request.messageType(),
                request.dataset(),
                request.dataName(),
                request.criticality()));
    String location = "/api/v1/topics/%s".formatted(output.id());
    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, location)
        .body(output);
  }

  @GetMapping
  public ResponseEntity<List<ListTopicsUseCaseOutput>> index() {
    LOGGER.info("[flow:list.topic] Receiving HTTP request to list topics");
    return ResponseEntity.ok(this.listTopicsUseCase.execute());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") @NotNull @NotBlank String topicId) {
    LOGGER.info("[flow:delete.topic] Receiving HTTP request to delete topic with id {}", topicId);
    this.deleteTopicUseCase.execute(topicId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public GetTopicByNameUseCaseOutput getById(
      @PathVariable("id") @NotNull @NotBlank String topicId) {
    return this.getTopicByIdUseCase.execute(topicId);
  }
}
