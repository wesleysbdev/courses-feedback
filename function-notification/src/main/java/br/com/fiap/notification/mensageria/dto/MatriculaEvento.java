package br.com.fiap.notification.mensageria.dto;

import java.util.UUID;

public record MatriculaEvento(
        int id,
        UsuarioSimplificadoEvento aluno,
        CursoEvento curso,
        StatusMatriculaEvento status
) {
}
