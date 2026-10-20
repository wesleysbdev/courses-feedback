package br.com.fiap.feedback.feedback.web.dto;

import br.com.fiap.feedback.feedback.domain.vo.FeedbackAvaliacao;

import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        UUID matriculaId,
        String descricao,
        FeedbackAvaliacao avaliacao
) {
}
