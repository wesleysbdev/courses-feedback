package br.com.fiap.feedback.feedback.service.notificacao;

import br.com.fiap.feedback.feedback.mensageria.dto.FeedbackNotificacaoEvento;

public interface NotificacaoService {
    void notificar(FeedbackNotificacaoEvento notificacao);
}
