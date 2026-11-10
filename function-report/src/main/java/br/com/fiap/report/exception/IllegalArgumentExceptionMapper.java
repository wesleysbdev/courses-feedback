package br.com.fiap.report.exception;

import br.com.fiap.report.dto.response.ErroResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {

        var erro = new ErroResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(erro)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
