package br.com.fiap.feedback.infraestructure.mensageria.rabbitmq;

import br.com.fiap.feedback.feedback.service.notificacao.MessagePublisher;
import br.com.fiap.feedback.feedback.service.notificacao.MessagingDestination;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "app.messaging",
        name = "provider",
        havingValue = "rabbitmq",
        matchIfMissing = true
)
public class RabbitMqMessagePublisher implements MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(MessagingDestination destination, Object message) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE,
                resolveRoutingKey(destination),
                message
        );
    }

    private String resolveRoutingKey(MessagingDestination destination) {
        return switch (destination) {
            case FEEDBACK_RUIM -> RabbitMqConfig.ROUTING_KEY;
        };
    }
}
