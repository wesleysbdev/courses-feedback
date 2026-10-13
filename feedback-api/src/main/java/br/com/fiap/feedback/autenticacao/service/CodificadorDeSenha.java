package br.com.fiap.feedback.autenticacao.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CodificadorDeSenha {
    private final PasswordEncoder encoder;

    public String codificar(String senhaEmTexto) {
        return encoder.encode(senhaEmTexto);
    }

    public boolean correspondente(String senhaHash, String senhaEmTexto) {
        return encoder.matches(senhaEmTexto, senhaHash);
    }
}
