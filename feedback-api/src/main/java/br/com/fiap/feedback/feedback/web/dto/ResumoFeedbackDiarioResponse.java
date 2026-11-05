package br.com.fiap.feedback.feedback.web.dto;

import java.time.LocalDate;

public record ResumoFeedbackDiarioResponse(
        LocalDate data,
        long somaNotas,
        long totalAvaliacoes,
        long avaliacoesCriticas,
        long avaliacoesNormais,
        long avaliacoesBoas
) {
}
