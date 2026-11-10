package br.com.fiap.report.controller;

import br.com.fiap.report.dto.response.FeedbackRelatorioResponse;
import br.com.fiap.report.service.RelatorioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;

@Path("/feedbacks/relatorio")
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioController {

    private final RelatorioService service;

    @Inject
    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GET
    @Path("/inicio/{inicio}/fim/{fim}")
    public FeedbackRelatorioResponse gerarRelatorio(@PathParam("inicio") LocalDate inicio, @PathParam("fim") LocalDate fim) {
        return service.gerarRelatorio(inicio, fim);
    }
}