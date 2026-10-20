package br.com.fiap.feedback.feedback.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class FeedbackNaoEncontradoException extends RegraDeNegocioException {
    public FeedbackNaoEncontradoException() {
        super("Feedback não encontrado.");
    }

    public FeedbackNaoEncontradoException(String message) {
        super(message);
    }
}
