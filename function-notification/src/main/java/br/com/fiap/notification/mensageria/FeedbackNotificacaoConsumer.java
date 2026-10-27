package br.com.fiap.notification.mensageria;

import br.com.fiap.notification.mensageria.dto.FeedbackNotificacaoEvento;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.io.IOException;

@ApplicationScoped
public class FeedbackNotificacaoConsumer {

    private static final Logger LOG = Logger.getLogger(FeedbackNotificacaoConsumer.class);
    private final ObjectMapper objectMapper;

    @Inject
    public FeedbackNotificacaoConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Incoming("feedback-notificacoes")
    public void consumir(byte[] payload) {
        LOG.info("Mensagem recebida do RabbitMQ.");

        FeedbackNotificacaoEvento evento = desserializar(payload);

        validar(evento);

        LOG.infof(
                "Evento de feedback consumido. id=%s, versao=%d, nota=%d",
                evento.feedbackId(),
                evento.versao(),
                evento.avaliacao().nota()
        );
    }

    private FeedbackNotificacaoEvento desserializar(byte[] payload) {
        try {
            return objectMapper.readValue(payload, FeedbackNotificacaoEvento.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Não foi possível interpretar o evento de feedback.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validar(FeedbackNotificacaoEvento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("O evento de feedback não pode ser nulo.");
        }

        if (evento.feedbackId() == null) {
            throw new IllegalArgumentException("O identificador do evento é obrigatório.");
        }

        if (evento.versao() != 1) {
            throw new IllegalArgumentException("Versão de evento não suportada: " + evento.versao());
        }

        if (evento.descricao() == null || evento.descricao().isBlank()) {
            throw new IllegalArgumentException("A descrição do feedback é obrigatória.");
        }

        if (evento.avaliacao() == null
                || evento.avaliacao().nota() == null
                || evento.avaliacao().nota() < 0
                || evento.avaliacao().nota() > 10) {
            throw new IllegalArgumentException("A avaliação do feedback é inválida.");
        }

        if (evento.matricula() == null
                || evento.matricula().isNull()
                || !evento.matricula().isObject()) {
            throw new IllegalArgumentException("Os dados da matrícula são obrigatórios.");
        }
    }
}
