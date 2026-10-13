package br.com.fiap.feedback.autenticacao.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class CredenciaisInvalidasException extends RegraDeNegocioException {
    public CredenciaisInvalidasException() {
        super("Credenciais inválidas.");
    }
}
