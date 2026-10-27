package br.com.fiap.feedback.feedback.web.dto;

import br.com.fiap.feedback.feedback.domain.vo.FeedbackAvaliacao;
import br.com.fiap.feedback.matricula.web.dto.MatriculaResponse;

import java.util.UUID;

public record FeedbackNotificacao(
        int versao,
        UUID feedbackId,
        String descricao,
        FeedbackAvaliacao avaliacao,
        MatriculaResponse matricula
) {
}
