package br.com.fiap.feedback.infraestructure.mensageria;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "feedback.eventos";
    public static final String FILA = "notificacao.feedback.ruim";
    public static final String ROUTING_KEY = "feedback.ruim";

    @Bean
    public TopicExchange feedbackExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notificacaoFeedbackQueue() {
        return QueueBuilder.durable(FILA).build();
    }

    @Bean
    public Binding notificacaoFeedbackBinding(
            Queue notificacaoFeedbackQueue,
            TopicExchange feedbackExchange) {
        return BindingBuilder
                .bind(notificacaoFeedbackQueue)
                .to(feedbackExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
