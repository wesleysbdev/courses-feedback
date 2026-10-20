package br.com.fiap.feedback.curso.domain.entity;

import br.com.fiap.feedback.curso.domain.exception.CursoDadosInvalidosException;
import br.com.fiap.feedback.shared.UuidV7;
import br.com.fiap.feedback.usuario.domain.entity.Usuario;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioDadosInvalidosException;

import java.util.UUID;

public class Curso {
    private final UUID id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private Usuario professor;

    public Curso(UUID id, String nome, String descricao, boolean ativo, Usuario professor) {
        UuidV7.validate(id);
        validarDadosObrigatorios(nome, descricao, professor);
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.professor = professor;
    }

    public static Curso criar(String nome, String descricao, Usuario professor) {
        return new Curso(UuidV7.generate(), nome, descricao, true, professor);
    }

    private static void validarDadosObrigatorios(String nome, String descricao, Usuario professor) {
        if (nome == null || nome.isBlank()) {
            throw new CursoDadosInvalidosException("Nome é obrigatório.");
        }

        if (descricao == null || descricao.isBlank()) {
            throw new CursoDadosInvalidosException("A descrição é obrigatória.");
        }

        if (professor == null) {
            throw new CursoDadosInvalidosException("O professor é obrigatório.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Usuario getProfessor() {
        return professor;
    }
}
