package br.com.fiap.feedback.shared;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.autenticacao.domain.exception.UsuarioNaoAutorizadoException;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;

import java.util.Set;

public abstract class PermissaoValidator {

    public static void verificarPermissao(TipoUsuario tipoUsuario, Set<TipoUsuario> permissoesPermitidas) {
        if (!permissoesPermitidas.contains(tipoUsuario)) {
            throw new UsuarioNaoAutorizadoException();
        }
    }

    public static void permiteApenasAdmin(UsuarioAutenticado usuarioAutenticado) {
        verificarPermissao(usuarioAutenticado.tipo(), Set.of(TipoUsuario.ADMINISTRADOR));
    }

    public static void permiteApenasProfessor(UsuarioAutenticado usuarioAutenticado) {
        verificarPermissao(usuarioAutenticado.tipo(), Set.of(TipoUsuario.PROFESSOR));
    }

    public static void permiteApenasAluno(UsuarioAutenticado usuarioAutenticado) {
        verificarPermissao(usuarioAutenticado.tipo(), Set.of(TipoUsuario.ALUNO));
    }

}
