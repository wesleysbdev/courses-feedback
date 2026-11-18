package br.com.fiap.feedback.feedback.service.notificacao;

import br.com.fiap.feedback.feedback.mensageria.dto.FeedbackNotificacaoEvento;
import br.com.fiap.feedback.infraestructure.mensageria.rabbitmq.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final MessagePublisher messagePublisher;

    @Override
    public void notificar(FeedbackNotificacaoEvento notificacao) {
        messagePublisher.publish(
                MessagingDestination.FEEDBACK_RUIM,
                notificacao
        );
    }
}
