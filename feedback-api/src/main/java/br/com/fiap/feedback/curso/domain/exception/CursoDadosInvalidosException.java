package br.com.fiap.feedback.curso.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class CursoDadosInvalidosException extends RegraDeNegocioException {
    public CursoDadosInvalidosException(String message) {
        super(message);
    }
}
