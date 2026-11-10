package br.com.fiap.report.exception;

import br.com.fiap.report.dto.response.ErroResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class ExceptionMapperGenerico implements ExceptionMapper<Exception> {

    private static final Logger LOG =
            Logger.getLogger(ExceptionMapperGenerico.class);

    @Override
    public Response toResponse(Exception exception) {

        LOG.error("Erro inesperado ao processar a requisição.", exception);

        var erro = new ErroResponse(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocorreu um erro interno ao processar a solicitação."
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(erro)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}