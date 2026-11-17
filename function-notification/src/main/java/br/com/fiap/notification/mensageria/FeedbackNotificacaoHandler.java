
package br.com.fiap.notification.mensageria;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FeedbackNotificacaoHandler
        implements RequestHandler<SQSEvent, SQSBatchResponse> {

    private static final Logger LOG =
            Logger.getLogger(FeedbackNotificacaoHandler.class);

    private final FeedbackNotificacaoProcessor processor;

    @Inject
    public FeedbackNotificacaoHandler(
            FeedbackNotificacaoProcessor processor
    ) {
        this.processor = processor;
    }


    @Override
    public SQSBatchResponse handleRequest(
            SQSEvent event,
            Context context
    ) {
        List<SQSBatchResponse.BatchItemFailure> failures =
                new ArrayList<>();

        if (event == null || event.getRecords() == null) {
            LOG.error("Evento SQS ou lista de mensagens não informado.");
            return new SQSBatchResponse(failures);
        }

        for (SQSEvent.SQSMessage message : event.getRecords()) {
            try {
                if (message == null || message.getMessageId() == null) {
                    throw new IllegalArgumentException(
                            "Mensagem SQS ou identificador não informado."
                    );
                }

                LOG.infof(
                        "Processando mensagem SQS. messageId=%s",
                        message.getMessageId()
                );

                if (message.getBody() == null || message.getBody().isBlank()) {
                    throw new IllegalArgumentException(
                            "O corpo da mensagem SQS não pode ser vazio."
                    );
                }

                byte[] payload = message.getBody()
                        .getBytes(StandardCharsets.UTF_8);

                processor.processar(payload);

            } catch (Exception exception) {
                String messageId = message != null
                        ? message.getMessageId()
                        : null;

                LOG.errorf(
                        exception,
                        "Falha ao processar mensagem SQS. messageId=%s",
                        messageId
                );

                if (messageId != null) {
                    failures.add(
                            new SQSBatchResponse.BatchItemFailure(messageId)
                    );
                } else {
                    // Não é possível informar a falha individual sem o ID.
                    // Falhar a invocação inteira permite nova tentativa do lote.
                    throw new IllegalStateException(
                            "Não foi possível identificar a mensagem com falha.",
                            exception
                    );
                }
            }
        }

        LOG.infof(
                "Lote processado. total=%d, falhas=%d",
                event.getRecords().size(),
                failures.size()
        );

        return new SQSBatchResponse(failures);
    }
}
