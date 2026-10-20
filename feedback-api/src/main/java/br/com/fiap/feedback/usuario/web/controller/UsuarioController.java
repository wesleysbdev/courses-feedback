package br.com.fiap.feedback.usuario.web.controller;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.infraestructure.security.SecurityContextProvider;
import br.com.fiap.feedback.usuario.domain.dto.AlteracaoSenha;
import br.com.fiap.feedback.usuario.service.UsuarioService;
import br.com.fiap.feedback.usuario.web.dto.AlterarSenhaRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioCadastroRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioEdicaoRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;
    private final SecurityContextProvider contextProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody @Valid UsuarioCadastroRequest request) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.cadastrar(request, usuarioAutenticado);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioResponse> listarTodos() {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.listarTodos(usuarioAutenticado);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponse consultarPorId(@PathVariable UUID id) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.buscarPorId(id, usuarioAutenticado);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponse editarAdministrador(@PathVariable UUID id, @RequestBody @Valid UsuarioEdicaoRequest request) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.atualizar(id, request, usuarioAutenticado);
    }

    @PatchMapping("/{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable UUID id, @RequestBody @Valid AlterarSenhaRequest request) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        service.alterarSenhaUsuario(id, new AlteracaoSenha(request.novaSenha()), usuarioAutenticado);
    }

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarUsuario(@PathVariable UUID id) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        service.ativarUsuario(id, usuarioAutenticado);
    }

    @PatchMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarUsuario(@PathVariable UUID id) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        service.inativarUsuario(id, usuarioAutenticado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        service.excluirUsuario(id, usuarioAutenticado);
    }
}
