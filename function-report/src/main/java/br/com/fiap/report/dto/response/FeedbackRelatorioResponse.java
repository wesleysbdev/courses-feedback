package br.com.fiap.report.dto.response;

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
