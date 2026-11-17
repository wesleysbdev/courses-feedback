package br.com.fiap.feedback.feedback.service.notificacao;

public interface MessagePublisher {
    void publish(MessagingDestination destination, Object message);
}
