package dev.matheuscruz.kafkamoon.api.presentation.controller.advice;

import dev.matheuscruz.kafkamoon.api.domain.topics.TopicNameExceededException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @Value("${kafkamoon.docsUrl}")
   private String docsUrl;

   @ExceptionHandler(TopicExistsException.class)
   public ResponseEntity<ProblemDetail> topicExistsExceptionHandler(final TopicExistsException e) {
      ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
      problemDetail.setDetail(e.getMessage().concat("."));
      problemDetail.setType(URI.create("%s/#/reference/api?id=topic-with-conflict".formatted(docsUrl)));
      return ResponseEntity.of(problemDetail).build();
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ProblemDetail> methodArgumentNotValidExceptionHandler(
         final MethodArgumentNotValidException e) {
      ProblemDetail body = e.getBody();
      body.setType(URI.create("%s/#/reference/api?id=invalid-request".formatted(docsUrl)));
      return ResponseEntity.of(body).build();
   }

   @ExceptionHandler(TopicNameExceededException.class)
   public ResponseEntity<ProblemDetail> topicNameExceededException(TopicNameExceededException e) {
      ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
      problemDetail.setType(URI.create("%s/#/reference/api?id=topic-name-exceeded-limit".formatted(docsUrl)));
      problemDetail.setDetail(e.getMessage());
      return ResponseEntity.of(problemDetail).build();
   }

}
