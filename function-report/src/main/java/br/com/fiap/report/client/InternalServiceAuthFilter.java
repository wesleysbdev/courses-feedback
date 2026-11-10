package br.com.fiap.report.client;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
public class InternalServiceAuthFilter implements ClientRequestFilter {

    private static final String INTERNAL_TOKEN_HEADER = "X-Internal-Service-Token";

    @ConfigProperty(name = "app.security.internal-token")
    private String internalToken;

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext.getHeaders().putSingle(
                INTERNAL_TOKEN_HEADER,
                internalToken
        );
    }
}
