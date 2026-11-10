package br.com.fiap.feedback.feedback.integracao.dto;

public record ErroIntegracaoResponse(
        int status,
        String erro,
        String mensagem
) {
}