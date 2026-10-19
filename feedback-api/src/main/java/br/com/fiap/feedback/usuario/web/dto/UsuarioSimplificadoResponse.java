package br.com.fiap.feedback.usuario.web.dto;


import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;

import java.util.UUID;

public record UsuarioSimplificadoResponse(
        UUID id,
        String nome
) {
}
