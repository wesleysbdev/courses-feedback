package br.com.fiap.feedback.curso.web.dto;

import br.com.fiap.feedback.usuario.web.dto.UsuarioSimplificadoResponse;

import java.util.UUID;

public record CursoResponse(
        UUID id,
        String nome,
        String descricao,
        UsuarioSimplificadoResponse professor
) {
}
