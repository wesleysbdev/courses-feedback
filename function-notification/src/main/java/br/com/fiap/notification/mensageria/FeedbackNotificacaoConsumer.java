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
                "Feedback consumido. feedbackId=%s, nota=%d, classificação=%s, aluno=%s, curso=%s",
                evento.feedbackId(),
                evento.avaliacao().nota(),
                evento.avaliacao().getClassificacao(),
                evento.matricula().aluno().nome(),
                evento.matricula().curso().nome()
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
                || evento.matricula().id() == null
                || evento.matricula().aluno() == null
                || evento.matricula().curso() == null
                || evento.matricula().status() == null) {
            throw new IllegalArgumentException("Os dados da matrícula são inválidos.");
        }

        if (evento.matricula().aluno().id() == null
                || evento.matricula().curso().id() == null
                || evento.matricula().curso().professor() == null
                || evento.matricula().curso().professor().id() == null) {
            throw new IllegalArgumentException("Os dados do aluno, curso ou professor são inválidos.");
        }
    }
}
