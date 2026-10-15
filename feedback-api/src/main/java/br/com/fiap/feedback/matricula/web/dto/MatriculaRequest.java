package br.com.fiap.feedback.matricula.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MatriculaRequest(
        @NotNull
        UUID cursoId
) {
}
