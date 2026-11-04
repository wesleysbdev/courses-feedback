package br.com.fiap.notification.mensageria.dto;

public record FeedbackAvaliacaoEvento(
        Integer nota
) {
    public FeedbackClassificacaoEvento getClassificacao() {
        return switch (nota) {
            case 0, 1, 2, 3, 4 -> FeedbackClassificacaoEvento.RUIM;
            case 5, 6, 7 -> FeedbackClassificacaoEvento.MEDIO;
            case 8, 9, 10 -> FeedbackClassificacaoEvento.BOM;
            default -> throw new IllegalArgumentException(
                    "Nota inválida: " + nota
            );
        };
    }
}