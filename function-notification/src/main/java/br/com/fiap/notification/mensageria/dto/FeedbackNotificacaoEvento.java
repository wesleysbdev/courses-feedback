package br.com.fiap.notification.mensageria.dto;

import java.util.UUID;

public record FeedbackNotificacaoEvento(
        int versao,
        UUID feedbackId,
        String descricao,
        FeedbackAvaliacaoEvento avaliacao,
        MatriculaEvento matricula
) {
}
