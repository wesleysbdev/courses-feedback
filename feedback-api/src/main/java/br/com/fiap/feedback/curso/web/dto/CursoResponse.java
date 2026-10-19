package br.com.fiap.feedback.curso.web.dto;

import br.com.fiap.feedback.usuario.web.dto.UsuarioSimplificadoResponse;

public record CursoResponse(
        String nome,
        String descricao,
        UsuarioSimplificadoResponse professor
) {
}
