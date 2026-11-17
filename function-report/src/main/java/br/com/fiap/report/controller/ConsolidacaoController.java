package br.com.fiap.report.controller;

import br.com.fiap.report.service.ConsolidacaoDiariaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.ZoneId;

@Path("/internal/consolidacao")
@Produces(MediaType.APPLICATION_JSON)
public class ConsolidacaoController {

    private static final ZoneId ZONE_ID =
            ZoneId.of("America/Sao_Paulo");

    private final ConsolidacaoDiariaService service;

    @Inject
    public ConsolidacaoController(
            ConsolidacaoDiariaService service
    ) {
        this.service = service;
    }

    @POST
    public Response consolidar() {

        LocalDate data = LocalDate.now(ZONE_ID).minusDays(1);

        var resumo = service.consolidar(data);

        return Response.ok(resumo).build();
    }
}
