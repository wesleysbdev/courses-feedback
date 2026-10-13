package br.com.fiap.feedback.infraestructure.security;

import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserTokenValidator implements OAuth2TokenValidator<Jwt> {

    private final UsuarioRepository repository;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        try {
            UUID usuarioId = UUID.fromString(jwt.getSubject());

            Optional<UsuarioModel> usuarioEncontrado = repository.findById(usuarioId);

            if (usuarioEncontrado.isEmpty()) {
                return falha();
            }

            UsuarioModel usuario = usuarioEncontrado.get();

            if (!usuario.isAtivo() || usuario.isExcluido()) {
                return falha();
            }

            return OAuth2TokenValidatorResult.success();

        } catch (IllegalArgumentException | NullPointerException e) {
            return falha();
        }
    }

    private OAuth2TokenValidatorResult falha() {
        OAuth2Error erro = new OAuth2Error(
                "invalid_token",
                "Token inválido ou usuário sem acesso.",
                null
        );

        return OAuth2TokenValidatorResult.failure(erro);
    }
}
