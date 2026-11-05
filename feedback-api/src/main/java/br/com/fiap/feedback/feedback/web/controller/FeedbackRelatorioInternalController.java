package br.com.fiap.feedback.feedback.web.controller;

import br.com.fiap.feedback.feedback.service.relatorio.FeedbackRelatorioService;
import br.com.fiap.feedback.feedback.web.dto.ResumoFeedbackDiarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/internal/feedbacks/agregados-diarios")
@RequiredArgsConstructor
public class FeedbackRelatorioInternalController {
    private final FeedbackRelatorioService feedbackRelatorioService;

    @GetMapping("/{data}")
    public ResumoFeedbackDiarioResponse buscarResumoDiario(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data
    ) {
        return feedbackRelatorioService.buscarResumoDiario(data);
    }
}
