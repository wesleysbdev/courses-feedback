package br.com.fiap.feedback.usuario.domain.exception;

import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class UsuarioNaoEncontradoException extends RegraDeNegocioException {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }
}
