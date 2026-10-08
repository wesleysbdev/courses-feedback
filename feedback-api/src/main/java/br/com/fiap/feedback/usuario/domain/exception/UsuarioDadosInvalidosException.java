package br.com.fiap.feedback.usuario.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class UsuarioDadosInvalidosException extends RegraDeNegocioException {
    public UsuarioDadosInvalidosException(String message) {
        super(message);
    }
}
