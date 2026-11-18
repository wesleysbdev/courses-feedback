package br.com.fiap.feedback.feedback.mensageria.dto;

import java.util.UUID;

public record UsuarioSimplificadoEvento(
        UUID id,
        String nome
) {
}
