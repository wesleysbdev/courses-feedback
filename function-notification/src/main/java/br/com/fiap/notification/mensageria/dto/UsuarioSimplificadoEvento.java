package br.com.fiap.notification.mensageria.dto;

import java.util.UUID;

public record UsuarioSimplificadoEvento(
        UUID id,
        String nome
) {
}
