package br.com.fiap.feedback.autenticacao.domain.dto;

import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;

import java.util.UUID;

public record UsuarioAutenticado(
        UUID id,
        String nome,
        TipoUsuario tipo
) {
}
