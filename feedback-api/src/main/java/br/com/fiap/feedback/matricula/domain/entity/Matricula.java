package br.com.fiap.feedback.matricula.domain.entity;

import br.com.fiap.feedback.curso.domain.entity.Curso;
import br.com.fiap.feedback.matricula.domain.enums.StatusMatricula;
import br.com.fiap.feedback.shared.UuidV7;
import br.com.fiap.feedback.usuario.domain.entity.Usuario;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioDadosInvalidosException;

import java.util.UUID;

public class Matricula {
    private final UUID id;
    private Usuario aluno;
    private Curso curso;
    private StatusMatricula status;

    public Matricula(UUID id, Usuario aluno, Curso curso, StatusMatricula status) {
        UuidV7.validate(id);
        validarDadosObrigatorios(aluno, curso, status);
        this.id = id;
        this.aluno = aluno;
        this.curso = curso;
        this.status = status;
    }

    public static Matricula criar(Usuario aluno, Curso curso) {
        return new Matricula(UuidV7.generate(), aluno, curso, StatusMatricula.EM_ANDAMENTO);
    }

    private static void validarDadosObrigatorios(Usuario aluno, Curso curso, StatusMatricula status) {
        if (aluno == null) {
            throw new UsuarioDadosInvalidosException("O aluno é obrigatório.");
        }

        if (curso == null) {
            throw new UsuarioDadosInvalidosException("O curso é obrigatória.");
        }

        if (status == null) {
            throw new UsuarioDadosInvalidosException("O status é obrigatório.");
        }
    }

    public UUID getId() {
        return id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public Curso getCurso() {
        return curso;
    }

    public StatusMatricula getStatus() {
        return status;
    }
}
