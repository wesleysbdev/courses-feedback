package br.com.fiap.feedback.infraestructure.mensageria.sqs;

import br.com.fiap.feedback.feedback.service.notificacao.MessagePublisher;
import br.com.fiap.feedback.feedback.service.notificacao.MessagingDestination;
import br.com.fiap.feedback.infraestructure.mensageria.config.MessagingProperties;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "app.messaging",
        name = "provider",
        havingValue = "sqs"
)
public class SqsMessagePublisher implements MessagePublisher {

    private final SqsTemplate sqsTemplate;
    private final MessagingProperties messagingProperties;

    public SqsMessagePublisher(
            SqsTemplate sqsTemplate,
            MessagingProperties messagingProperties
    ) {
        this.sqsTemplate = sqsTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(MessagingDestination destination, Object message) {

        String queueName = switch (destination) {
            case FEEDBACK_RUIM -> messagingProperties.queueName();
        };

        sqsTemplate.send(queueName, message);
    }
}