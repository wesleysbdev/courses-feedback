package br.com.fiap.feedback.feedback.integracao;

import br.com.fiap.feedback.feedback.integracao.dto.ErroIntegracaoResponse;
import br.com.fiap.feedback.feedback.integracao.dto.FeedbackRelatorioResponse;
import br.com.fiap.feedback.feedback.integracao.exception.FunctionReportIntegracaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FunctionReportClient {

    @Qualifier("functionReportRestClient")
    private final RestClient restClient;

    public FeedbackRelatorioResponse gerarRelatorio(LocalDate inicio, LocalDate fim) {

        try {
            return restClient
                    .get()
                    .uri(
                            "/feedbacks/relatorio/inicio/{inicio}/fim/{fim}",
                            inicio,
                            fim
                    )
                    .retrieve()
                    .body(FeedbackRelatorioResponse.class);

        } catch (RestClientResponseException exception) {

            ErroIntegracaoResponse erro;

            try {
                erro = exception.getResponseBodyAs(ErroIntegracaoResponse.class);
            } catch (RestClientException e) {
                erro = new ErroIntegracaoResponse(
                        exception.getStatusCode().value(),
                        exception.getStatusText(),
                        "Ocorreu um erro ao processar a solicitação no serviço de relatórios."
                );
            }

            throw new FunctionReportIntegracaoException(exception.getStatusCode(), erro);
        }
    }
}