package br.com.fiap.feedback.autenticacao.service;

import br.com.fiap.feedback.autenticacao.domain.dto.Autenticacao;
import br.com.fiap.feedback.autenticacao.domain.exception.CredenciaisInvalidasException;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.persistence.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutenticacaoService {

    private final UsuarioRepository repository;
    private final CodificadorDeSenha codificador;
    private final TokenService tokenService;

    public String autenticar(Autenticacao autenticacao) {

        UsuarioModel usuario = repository.findByEmail(autenticacao.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (usuario.isExcluido() || !usuario.isAtivo()) {
            throw new CredenciaisInvalidasException();
        }

        if (!codificador.correspondente((usuario.getSenha()), autenticacao.senha())) {
            throw new CredenciaisInvalidasException();
        }

        return tokenService.gerar(usuario);
    }
}
