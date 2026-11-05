package br.com.fiap.feedback.feedback.service.relatorio;

import br.com.fiap.feedback.feedback.persistence.projection.ResumoFeedbackDiarioProjection;
import br.com.fiap.feedback.feedback.persistence.repository.FeedbackRepository;
import br.com.fiap.feedback.feedback.web.dto.ResumoFeedbackDiarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackRelatorioService {
    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public ResumoFeedbackDiarioResponse buscarResumoDiario(LocalDate data) {

        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();

        ResumoFeedbackDiarioProjection resumo = feedbackRepository.buscarResumoDiario(
                data,
                inicio,
                fim
        );

        return new ResumoFeedbackDiarioResponse(
                resumo.data(),
                resumo.somaNotas(),
                resumo.totalAvaliacoes(),
                resumo.avaliacoesCriticas(),
                resumo.avaliacoesNormais(),
                resumo.avaliacoesBoas()
        );
    }
}
