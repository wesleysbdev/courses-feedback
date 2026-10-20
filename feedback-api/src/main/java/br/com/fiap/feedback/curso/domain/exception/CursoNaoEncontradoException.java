package br.com.fiap.feedback.curso.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class CursoNaoEncontradoException extends RegraDeNegocioException {
    public CursoNaoEncontradoException() {
        super("Curso não encontrado.");
    }

    public CursoNaoEncontradoException(String message) {
        super(message);
    }
}
