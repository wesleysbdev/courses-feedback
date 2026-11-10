package br.com.fiap.report.scheduler;

import br.com.fiap.report.service.ConsolidacaoDiariaService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.time.ZoneId;

@ApplicationScoped
public class ConsolidacaoDiariaScheduler {

    private static final Logger LOG = Logger.getLogger(ConsolidacaoDiariaScheduler.class);
    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");
    private final ConsolidacaoDiariaService service;

    @Inject
    public ConsolidacaoDiariaScheduler(ConsolidacaoDiariaService service) {
        this.service = service;
    }

    //    @Scheduled(every = "60s")
    @Scheduled(cron = "0 10 0 * * ?", timeZone = "America/Sao_Paulo")
    void consolidarDiaAnterior() {

        LocalDate data = LocalDate.now(ZONE_ID).minusDays(1);

        LOG.infof("Iniciando consolidação dos feedbacks do dia %s", data);

        try {
            var resumo = service.consolidar(data);

            LOG.infof(
                    "Consolidação concluída. Data: %s | Total de avaliações: %d",
                    data,
                    resumo.totalAvaliacoes()
            );

        } catch (RuntimeException e) {

            LOG.errorf(
                    e,
                    "Falha ao consolidar os feedbacks do dia %s",
                    data
            );

            throw e;
        }
    }
}