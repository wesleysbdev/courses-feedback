package br.com.fiap.feedback.usuario.web.dto;

import br.com.fiap.feedback.infraestructure.annotation.validarenum.ValidarEnum;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCadastroRequest(
        @NotBlank(message = "O campo nome é obrigatório.")
        String nome,
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O email informado não corresponde a um formato válido.")
        String email,
        @NotBlank(message = "O campo senha é obrigatório.")
        String senha,
        @NotBlank(message = "O campo tipo é obrigatório e deve ser um tipo válido.")
        @ValidarEnum(enumClass = TipoUsuario.class, nomeCampo = "tipo")
        String tipo
) {
}
