package br.com.fiap.notification.mensageria.dto;

import java.util.UUID;

public record CursoEvento(
        UUID id,
        String nome,
        String descricao,
        UsuarioSimplificadoEvento professor
) {
}
