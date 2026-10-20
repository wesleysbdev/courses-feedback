package br.com.fiap.feedback.feedback.domain.entity;

import br.com.fiap.feedback.feedback.domain.exception.FeedbackDadosInvalidosException;
import br.com.fiap.feedback.feedback.domain.vo.FeedbackAvaliacao;
import br.com.fiap.feedback.matricula.domain.entity.Matricula;
import br.com.fiap.feedback.shared.UuidV7;

import java.util.UUID;

public class Feedback {
    private final UUID id;
    private final Matricula matricula;
    private final String descricao;
    private final FeedbackAvaliacao avaliacao;

    public Feedback(UUID id, Matricula matricula, String descricao, FeedbackAvaliacao avaliacao) {
        validarDadosObrigatorios(matricula, descricao, avaliacao);
        this.id = id;
        this.matricula = matricula;
        this.descricao = descricao;
        this.avaliacao = avaliacao;
    }

    public static Feedback criar(Matricula matricula, String descricao, int nota) {
        return new Feedback(UuidV7.generate(), matricula, descricao, new FeedbackAvaliacao(nota));
    }

    private static void validarDadosObrigatorios(Matricula matricula, String descricao, FeedbackAvaliacao avaliacao) {
        if (matricula == null) {
            throw new FeedbackDadosInvalidosException("A matricula do feedback é obrigatória.");
        }

        if (descricao == null || descricao.isBlank()) {
            throw new FeedbackDadosInvalidosException("A descrição do feedback é obrigatória.");
        }

        if (avaliacao == null) {
            throw new FeedbackDadosInvalidosException("A nota do feedback é obrigatória.");
        }
    }

    public UUID getId() {
        return id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public String getDescricao() {
        return descricao;
    }

    public FeedbackAvaliacao getAvaliacao() {
        return avaliacao;
    }
}
