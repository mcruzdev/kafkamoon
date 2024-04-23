package dev.matheuscruz.kafkamoon.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTopicRequest(@NotBlank @NotNull String messageType, @NotBlank @NotNull String dataset,
      @NotBlank @NotNull String dataName, @Pattern(regexp = "^(TEST|test)$") String criticality) {
}
