package br.com.fiap.feedback.feedback.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class FeedbackDadosInvalidosException extends RegraDeNegocioException {
    public FeedbackDadosInvalidosException(String message) {
        super(message);
    }
}
