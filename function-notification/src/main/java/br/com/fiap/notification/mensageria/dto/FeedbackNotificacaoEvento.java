package br.com.fiap.notification.mensageria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record FeedbackNotificacaoEvento(
        UUID feedbackId,
        int versao,
        String descricao,
        AvaliacaoPayload avaliacao,
        JsonNode matricula
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AvaliacaoPayload(Integer nota) {
    }
}
