package br.com.fiap.report.security;

import br.com.fiap.report.dto.response.ErroResponse;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class InternalServiceAuthenticationFilter implements ContainerRequestFilter {

    private static final String INTERNAL_PATH = "/feedbacks/relatorio/";
    private static final String INTERNAL_TOKEN_HEADER = "X-Internal-Service-Token";

    @ConfigProperty(name = "app.security.internal-token")
    String internalToken;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String path = requestContext.getUriInfo().getPath();

        if (!path.startsWith(INTERNAL_PATH)) {
            return;
        }

        String tokenRecebido = requestContext.getHeaderString(INTERNAL_TOKEN_HEADER);

        if (tokenRecebido == null ||
                !MessageDigest.isEqual(
                        internalToken.getBytes(StandardCharsets.UTF_8),
                        tokenRecebido.getBytes(StandardCharsets.UTF_8))) {

            var erro = new ErroResponse(
                    Response.Status.UNAUTHORIZED.getStatusCode(),
                    Response.Status.UNAUTHORIZED.getReasonPhrase(),
                    "Autenticação interna inválida."
            );

            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(erro)
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
    }
}
