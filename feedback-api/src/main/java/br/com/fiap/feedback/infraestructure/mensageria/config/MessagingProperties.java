package br.com.fiap.feedback.infraestructure.mensageria.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public record MessagingProperties(
        Provider provider,
        String queueName
) {
    public enum Provider {
        RABBITMQ,
        SQS
    }
}