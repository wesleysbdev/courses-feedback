package br.com.fiap.feedback.feedback.domain.vo;

import br.com.fiap.feedback.feedback.domain.enums.FeedbackClassificacao;
import br.com.fiap.feedback.feedback.domain.exception.FeedbackDadosInvalidosException;

public record FeedbackAvaliacao(int nota) {
    public FeedbackAvaliacao {
        if (nota < 0 || nota > 10) {
            throw new FeedbackDadosInvalidosException("A nota deve estar entre 0 e 10.");
        }
    }

    public FeedbackClassificacao getClassificacao() {
        return switch (nota) {
            case 0, 1, 2, 3 -> FeedbackClassificacao.RUIM;
            case 4, 5, 6 -> FeedbackClassificacao.MEDIO;
            default -> FeedbackClassificacao.BOM;
        };
    }
}
