package br.com.fiap.report.exception;

import java.time.LocalDate;

public class ResumoDiarioNaoEncontradoException extends RuntimeException {

    public ResumoDiarioNaoEncontradoException(LocalDate data) {
        super("Não existe resumo diário consolidado para a data " + data + ".");
    }
}
