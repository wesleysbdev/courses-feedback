package br.com.fiap.feedback.feedback.integracao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record FeedbackRelatorioResponse(
        PeriodoRelatorioResponse periodo,
        BigDecimal mediaNotas,
        Long totalAvaliacoes,
        Long avaliacoesCriticas,
        Long avaliacoesNormais,
        Long avaliacoesBoas,
        Map<LocalDate, Long> quantidadePorDia
) {
}
