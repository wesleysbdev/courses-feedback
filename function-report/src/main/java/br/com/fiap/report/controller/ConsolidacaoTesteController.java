package br.com.fiap.report.controller;

import br.com.fiap.report.dto.response.ResumoFeedbackDiarioResponse;
import br.com.fiap.report.service.ConsolidacaoDiariaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;

@Path("/teste/consolidacao")
@Produces(MediaType.APPLICATION_JSON)
public class ConsolidacaoTesteController {

    private final ConsolidacaoDiariaService service;

    @Inject
    public ConsolidacaoTesteController(ConsolidacaoDiariaService service) {
        this.service = service;
    }

    @GET
    @Path("/{data}")
    public ResumoFeedbackDiarioResponse consolidar(@PathParam("data") LocalDate data) {
        return service.consolidar(data);
    }
}