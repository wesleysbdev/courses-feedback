package br.com.fiap.feedback.matricula.web.dto;

import br.com.fiap.feedback.curso.web.dto.CursoResponse;
import br.com.fiap.feedback.matricula.domain.enums.StatusMatricula;
import br.com.fiap.feedback.usuario.web.dto.UsuarioSimplificadoResponse;

import java.util.UUID;

public record MatriculaResponse(
        UUID id,
        UsuarioSimplificadoResponse aluno,
        CursoResponse curso,
        StatusMatricula status
) {
}
