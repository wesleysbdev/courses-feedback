package br.com.fiap.feedback.feedback.mensageria.dto;

import java.util.UUID;

public record MatriculaEvento(
        UUID id,
        UsuarioSimplificadoEvento aluno,
        CursoEvento curso,
        StatusMatriculaEvento status
) {
}
