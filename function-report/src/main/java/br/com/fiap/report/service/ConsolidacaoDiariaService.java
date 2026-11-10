package br.com.fiap.report.service;

import br.com.fiap.report.client.FeedbackApiClient;
import br.com.fiap.report.dto.response.ResumoFeedbackDiarioResponse;
import br.com.fiap.report.redis.ResumoFeedbackRedisRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@ApplicationScoped
public class ConsolidacaoDiariaService {

    private final FeedbackApiClient feedbackApiClient;
    private final ResumoFeedbackRedisRepository repository;

    @Inject
    public ConsolidacaoDiariaService(
            @RestClient FeedbackApiClient feedbackApiClient,
            ResumoFeedbackRedisRepository repository
    ) {
        this.feedbackApiClient = feedbackApiClient;
        this.repository = repository;
    }

    public ResumoFeedbackDiarioResponse consolidar(LocalDate data) {
        var resumo = feedbackApiClient.buscarResumoDiario(data);
        repository.salvar(resumo);
        return resumo;
    }
}
