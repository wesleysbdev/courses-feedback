package br.com.fiap.report.dto.response;

import java.time.LocalDate;

public record PeriodoRelatorioResponse(
        LocalDate inicio,
        LocalDate fim
) {
}
