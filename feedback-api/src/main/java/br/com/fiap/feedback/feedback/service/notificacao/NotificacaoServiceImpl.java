package br.com.fiap.feedback.feedback.service.notificacao;

import br.com.fiap.feedback.feedback.web.dto.FeedbackNotificacao;
import br.com.fiap.feedback.infraestructure.mensageria.rabbitmq.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final MessagePublisher messagePublisher;

    @Override
    public void notificar(FeedbackNotificacao notificacao) {
        messagePublisher.publish(
                MessagingDestination.FEEDBACK_RUIM,
                notificacao
        );
    }
}
