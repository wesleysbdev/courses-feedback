package br.com.fiap.report.controller;

import br.com.fiap.report.client.FeedbackApiClient;
import br.com.fiap.report.dto.response.ResumoFeedbackDiarioResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@Path("/teste/feedback-api")
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackApiTesteController {

    @RestClient
    FeedbackApiClient feedbackApiClient;

    @GET
    @Path("/{data}")
    public ResumoFeedbackDiarioResponse buscarResumoDiario(@PathParam("data") LocalDate data) {
        return feedbackApiClient.buscarResumoDiario(data);
    }
}
