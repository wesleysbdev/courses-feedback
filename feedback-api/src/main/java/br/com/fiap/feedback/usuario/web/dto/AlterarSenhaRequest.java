package br.com.fiap.feedback.usuario.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequest(
        @NotBlank(message = "A nova senha é obrigatória.")
        String novaSenha
) {
}
