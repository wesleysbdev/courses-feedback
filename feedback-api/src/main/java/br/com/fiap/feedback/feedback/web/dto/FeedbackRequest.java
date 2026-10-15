package br.com.fiap.feedback.feedback.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FeedbackRequest(
        @NotNull
        UUID matriculaId,
        @NotBlank
        String descricao,
        @NotNull
        @Min(0)
        @Max(10)
        int nota
) {
}
