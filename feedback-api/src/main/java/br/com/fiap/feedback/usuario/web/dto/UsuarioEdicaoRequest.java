package br.com.fiap.feedback.usuario.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioEdicaoRequest(
        @NotBlank(message = "O campo nome é obrigatório.")
        String nome,
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O email informado não corresponde a um formato válido.")
        String email
) {
}
