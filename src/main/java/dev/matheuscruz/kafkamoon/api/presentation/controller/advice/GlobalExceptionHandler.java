package dev.matheuscruz.kafkamoon.api.presentation.controller.advice;

import dev.matheuscruz.kafkamoon.api.application.model.topic.TopicNameExceededException;
import dev.matheuscruz.kafkamoon.api.infrastructure.kafka.KafkaCommunicationException;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.MetricName;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Metrics;
import dev.matheuscruz.kafkamoon.api.infrastructure.o11y.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Value("${kafkamoon.docs.topic-name-exceeded-limit}")
  private String docsTopicNameExceededLimitUrl;

  @Value("${kafkamoon.docs.invalid-request}")
  private String docsInvalidRequestUrl;

  @Value("${kafkamoon.docs.topic-with-conflict}")
  private String docsTopicWithConflictUrl;

  @Value("${kafkamoon.docs.entity-not-found}")
  private String docsEntityNotFoundUrl;

  @Value("${kafkamoon.docs.kafka-communication-failure}")
  private String docsKafkaCommunicationFailure;

  @Value("${kafkamoon.kafka.bootstrap.servers}")
  private String kafkaBootstrapServers;

  private final Metrics metrics;

  public GlobalExceptionHandler(Metrics metrics) {
    this.metrics = metrics;
  }

  @ExceptionHandler(TopicExistsException.class)
  public ResponseEntity<ProblemDetail> topicExistsExceptionHandler(final TopicExistsException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problemDetail.setDetail(e.getMessage());
    problemDetail.setType(URI.create(docsTopicWithConflictUrl));
    return ResponseEntity.of(problemDetail).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> methodArgumentNotValidExceptionHandler(
      final MethodArgumentNotValidException e) {
    ProblemDetail body = e.getBody();
    body.setType(URI.create(docsInvalidRequestUrl));
    return ResponseEntity.of(body).build();
  }

  @ExceptionHandler(TopicNameExceededException.class)
  public ResponseEntity<ProblemDetail> topicNameExceededException(TopicNameExceededException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setType(URI.create(docsTopicNameExceededLimitUrl));
    problemDetail.setDetail(e.getMessage());
    return ResponseEntity.of(problemDetail).build();
  }

  @ExceptionHandler(UnknownTopicIdException.class)
  public ResponseEntity<ProblemDetail> unknownTopicIdException(UnknownTopicIdException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setType(URI.create(docsEntityNotFoundUrl));
    problemDetail.setDetail(e.getMessage());
    return ResponseEntity.of(problemDetail).build();
  }

  @ExceptionHandler(KafkaCommunicationException.class)
  public ResponseEntity<ProblemDetail> kafkaCommunicationException(
      KafkaCommunicationException e, HttpServletRequest httpServletRequest) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
    problemDetail.setType(URI.create(docsKafkaCommunicationFailure));
    problemDetail.setDetail(e.getMessage());

    String uri = httpServletRequest.getRequestURI();
    String method = httpServletRequest.getMethod();

    LOGGER.error(
        "[uri:{}][method:{}] Error while communicating with Kafka cluster ({}). Details: {}",
        httpServletRequest.getPathInfo(),
        method,
        kafkaBootstrapServers,
        e.getCause().getMessage());

    metrics.increment(
        MetricName.KAFKA_COMMUNICATION_FAILURE,
        Tag.create("uri", uri),
        Tag.create("method", method));
    return ResponseEntity.of(problemDetail).build();
  }
}
