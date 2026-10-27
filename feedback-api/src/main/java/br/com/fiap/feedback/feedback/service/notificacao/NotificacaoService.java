package br.com.fiap.feedback.feedback.service.notificacao;

import br.com.fiap.feedback.feedback.web.dto.FeedbackNotificacao;

public interface NotificacaoService {
    void notificar(FeedbackNotificacao notificacao);
}
