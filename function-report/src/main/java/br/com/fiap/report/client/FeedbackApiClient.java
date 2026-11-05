package br.com.fiap.report.client;

import br.com.fiap.report.dto.response.ResumoFeedbackDiarioResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.LocalDate;

@Path("/api/v1/internal/feedbacks/agregados-diarios")
@RegisterRestClient(configKey = "feedback-api")
@RegisterProvider(InternalServiceAuthFilter.class)
@Produces(MediaType.APPLICATION_JSON)
public interface FeedbackApiClient {

    @GET
    @Path("/{data}")
    ResumoFeedbackDiarioResponse buscarResumoDiario(@PathParam("data") LocalDate data);
}
