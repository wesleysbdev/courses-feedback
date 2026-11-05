package br.com.fiap.feedback.feedback.persistence.projection;

import java.time.LocalDate;

public record ResumoFeedbackDiarioProjection(
        LocalDate data,
        Long somaNotas,
        Long totalAvaliacoes,
        Long avaliacoesCriticas,
        Long avaliacoesNormais,
        Long avaliacoesBoas
) {
}
