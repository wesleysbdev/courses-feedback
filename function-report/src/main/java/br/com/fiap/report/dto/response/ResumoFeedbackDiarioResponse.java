package br.com.fiap.report.dto.response;

import java.time.LocalDate;

public record ResumoFeedbackDiarioResponse(
        LocalDate data,
        Long somaNotas,
        Long totalAvaliacoes,
        Long avaliacoesCriticas,
        Long avaliacoesNormais,
        Long avaliacoesBoas
) {
}
