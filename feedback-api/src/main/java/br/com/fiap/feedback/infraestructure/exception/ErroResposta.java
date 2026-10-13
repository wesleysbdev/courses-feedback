package br.com.fiap.feedback.infraestructure.exception;

import java.time.Instant;
import java.util.List;

public record ErroResposta(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        List<CampoErro> campos
) {
    public record CampoErro(String campo, String mensagem) {
    }
}
