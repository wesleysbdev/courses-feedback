package br.com.fiap.report.service;

import br.com.fiap.report.dto.response.FeedbackRelatorioResponse;
import br.com.fiap.report.dto.response.PeriodoRelatorioResponse;
import br.com.fiap.report.dto.response.ResumoFeedbackDiarioResponse;
import br.com.fiap.report.exception.ResumoDiarioNaoEncontradoException;
import br.com.fiap.report.redis.ResumoFeedbackRedisRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class RelatorioService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");
    private final ResumoFeedbackRedisRepository repository;
    @ConfigProperty(name = "app.security.maximo-dias-relatorio")
    private int MAXIMO_DIAS_PERIODO;

    @Inject
    public RelatorioService(ResumoFeedbackRedisRepository repository) {
        this.repository = repository;
    }

    public FeedbackRelatorioResponse gerarRelatorio(LocalDate inicio, LocalDate fim) {

        validarPeriodo(inicio, fim);

        long somaNotas = 0;
        long totalAvaliacoes = 0;
        long avaliacoesCriticas = 0;
        long avaliacoesNormais = 0;
        long avaliacoesBoas = 0;

        Map<LocalDate, Long> quantidadePorDia = new LinkedHashMap<>();

        for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {

            LocalDate dataAtual = data;
            ResumoFeedbackDiarioResponse resumo = repository.buscarPorData(data)
                    .orElseThrow(() -> new ResumoDiarioNaoEncontradoException(dataAtual));

            somaNotas += resumo.somaNotas();
            totalAvaliacoes += resumo.totalAvaliacoes();
            avaliacoesCriticas += resumo.avaliacoesCriticas();
            avaliacoesNormais += resumo.avaliacoesNormais();
            avaliacoesBoas += resumo.avaliacoesBoas();

            quantidadePorDia.put(data, resumo.totalAvaliacoes());
        }

        BigDecimal mediaNotas = calcularMedia(somaNotas, totalAvaliacoes);

        return new FeedbackRelatorioResponse(
                new PeriodoRelatorioResponse(inicio, fim),
                mediaNotas,
                totalAvaliacoes,
                avaliacoesCriticas,
                avaliacoesNormais,
                avaliacoesBoas,
                quantidadePorDia
        );
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {

        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas inicial e final são obrigatórias.");
        }

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
        }

        LocalDate ontem = LocalDate.now(ZONE_ID).minusDays(1);

        if (fim.isAfter(ontem)) {
            throw new IllegalArgumentException("A data final não pode ser posterior ao dia anterior.");
        }

        long quantidadeDias = ChronoUnit.DAYS.between(inicio, fim) + 1;

        if (quantidadeDias > MAXIMO_DIAS_PERIODO) {
            throw new IllegalArgumentException("O período solicitado não pode ultrapassar " + MAXIMO_DIAS_PERIODO + " dias.");
        }
    }

    private BigDecimal calcularMedia(long somaNotas, long totalAvaliacoes) {

        if (totalAvaliacoes == 0) {
            return null;
        }

        return BigDecimal.valueOf(somaNotas)
                .divide(
                        BigDecimal.valueOf(totalAvaliacoes),
                        1,
                        RoundingMode.HALF_UP
                );
    }
}