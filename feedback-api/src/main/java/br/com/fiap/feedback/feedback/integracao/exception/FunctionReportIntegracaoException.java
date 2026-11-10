package br.com.fiap.feedback.feedback.integracao.exception;

import br.com.fiap.feedback.feedback.integracao.dto.ErroIntegracaoResponse;
import org.springframework.http.HttpStatusCode;

public class FunctionReportIntegracaoException extends RuntimeException {

    private final HttpStatusCode status;
    private final ErroIntegracaoResponse erro;

    public FunctionReportIntegracaoException(HttpStatusCode status, ErroIntegracaoResponse erro) {
        super(erro.mensagem());
        this.status = status;
        this.erro = erro;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public ErroIntegracaoResponse getErro() {
        return erro;
    }
}
