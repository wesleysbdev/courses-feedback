package br.com.fiap.feedback.usuario.domain.entity;

import br.com.fiap.feedback.shared.UuidV7;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioDadosInvalidosException;
import br.com.fiap.feedback.usuario.domain.vo.Email;

import java.util.UUID;

public class Usuario {

    private final UUID id;
    private String nome;
    private Email email;
    private String senha;
    private boolean ativo;
    private boolean excluido;
    private TipoUsuario tipo;

    public Usuario(String nome, Email email, String senha, boolean ativo, boolean excluido, TipoUsuario tipo) {
        validarDadosObrigatorios(nome, email, senha, tipo);
        this.id = UuidV7.generate();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.excluido = excluido;
        this.tipo = tipo;
    }

    private static void validarDadosObrigatorios(String nome, Email email, String senha, TipoUsuario tipo) {
        if (nome == null || nome.isBlank()) {
            throw new UsuarioDadosInvalidosException("Nome é obrigatório.");
        }

        if (senha == null || senha.isBlank()) {
            throw new UsuarioDadosInvalidosException("A senha é obrigatória.");
        }

        if (tipo == null) {
            throw new UsuarioDadosInvalidosException("O tipo de usuário é obrigatório.");
        }
    }

    public void alterarDados(String nome, Email email) {
        if (nome == null || nome.isBlank()) {
            throw new UsuarioDadosInvalidosException("Nome é obrigatório.");
        }

        if (email == null) {
            throw new UsuarioDadosInvalidosException("Email é obrigatório.");
        }

        this.nome = nome;
        this.email = email;
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        if (excluido) {
            throw new UsuarioDadosInvalidosException(
                    "Usuário excluído não pode ser ativado."
            );
        }
        this.ativo = true;
    }

    public void excluir() {
        this.excluido = true;
    }

    public void definirNovaSenha(String senhaHash) {
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new UsuarioDadosInvalidosException("Nova senha é obrigatória.");
        }

        this.senha = senhaHash;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Email getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }
}
