package br.com.fiap.feedback.feedback.mensageria.dto;

import java.util.UUID;

public record FeedbackNotificacaoEvento(
        int versao,
        UUID feedbackId,
        String descricao,
        FeedbackAvaliacaoEvento avaliacao,
        MatriculaEvento matricula
) {
}
