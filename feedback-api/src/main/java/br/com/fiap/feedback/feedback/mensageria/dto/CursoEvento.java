package br.com.fiap.feedback.feedback.mensageria.dto;

import java.util.UUID;

public record CursoEvento(
        UUID id,
        String nome,
        String descricao,
        UsuarioSimplificadoEvento professor
) {
}
