package br.com.fiap.feedback.feedback.integracao.dto;

import java.time.LocalDate;

public record PeriodoRelatorioResponse(
        LocalDate inicio,
        LocalDate fim
) {
}
