package br.com.fiap.feedback.matricula.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class MatriculaNaoEncontradaException extends RegraDeNegocioException {
    public MatriculaNaoEncontradaException() {
        super("Matricula não encontrada.");
    }

    public MatriculaNaoEncontradaException(String message) {
        super(message);
    }
}
