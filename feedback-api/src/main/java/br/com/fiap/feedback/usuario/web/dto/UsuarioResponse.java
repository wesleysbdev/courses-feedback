package br.com.fiap.feedback.usuario.web.dto;


import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        TipoUsuario tipo,
        boolean ativo
) {
}
