package br.com.fiap.feedback.curso.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CursoRequest(
        @NotBlank
        @Size(max = 150)
        String nome,

        @Size(max = 1024, min = 10)
        String descricao,

        @NotNull
        UUID professorId
) {
}
