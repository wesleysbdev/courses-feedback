package br.com.fiap.feedback.matricula.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class MatriculaDadosInvalidosException extends RegraDeNegocioException {
    public MatriculaDadosInvalidosException(String message) {
        super(message);
    }
}
