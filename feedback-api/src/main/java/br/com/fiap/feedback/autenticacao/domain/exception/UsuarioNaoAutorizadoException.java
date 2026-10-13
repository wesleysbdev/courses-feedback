package br.com.fiap.feedback.autenticacao.domain.exception;


import br.com.fiap.feedback.infraestructure.exception.RegraDeNegocioException;

public class UsuarioNaoAutorizadoException extends RegraDeNegocioException {
    public UsuarioNaoAutorizadoException() {
        super("Usuário não tem permissão para realizar essa operação");
    }
}
