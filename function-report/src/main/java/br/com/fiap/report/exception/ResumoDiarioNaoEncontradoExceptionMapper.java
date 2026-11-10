package br.com.fiap.report.exception;

import br.com.fiap.report.dto.response.ErroResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResumoDiarioNaoEncontradoExceptionMapper implements ExceptionMapper<ResumoDiarioNaoEncontradoException> {

    @Override
    public Response toResponse(ResumoDiarioNaoEncontradoException exception) {

        var erro = new ErroResponse(
                Response.Status.SERVICE_UNAVAILABLE.getStatusCode(),
                Response.Status.SERVICE_UNAVAILABLE.getReasonPhrase(),
                exception.getMessage()
        );

        return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(erro)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}