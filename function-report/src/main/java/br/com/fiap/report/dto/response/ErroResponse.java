package br.com.fiap.report.dto.response;

public record ErroResponse(
        int status,
        String erro,
        String mensagem
) {
}
