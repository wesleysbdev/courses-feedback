package br.com.fiap.feedback.usuario.service;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.autenticacao.domain.exception.UsuarioNaoAutorizadoException;
import br.com.fiap.feedback.autenticacao.service.CodificadorDeSenha;
import br.com.fiap.feedback.shared.PermissaoValidator;
import br.com.fiap.feedback.usuario.domain.dto.AlteracaoSenha;
import br.com.fiap.feedback.usuario.domain.entity.Usuario;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioDadosInvalidosException;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioNaoEncontradoException;
import br.com.fiap.feedback.usuario.domain.vo.Email;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.persistence.repository.UsuarioRepository;
import br.com.fiap.feedback.usuario.web.dto.UsuarioCadastroRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioEdicaoRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioResponse;
import br.com.fiap.feedback.usuario.web.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final CodificadorDeSenha codificadorSenha;

    public List<UsuarioResponse> listarTodos(UsuarioAutenticado usuarioAutenticado) {
        permiteApenasAdmin(usuarioAutenticado);
        return repository.findByExcluidoFalse().stream().map(mapper::paraResponse).toList();
    }

    public UsuarioResponse buscarPorId(UUID usuarioId, UsuarioAutenticado usuarioAutenticado) {
        permiteOProprioOuAdmin(usuarioAutenticado, usuarioId);
        return mapper.paraResponse(encontrarPorId(usuarioId));
    }

    public List<UsuarioResponse> listarPorTipo(TipoUsuario tipoUsuario, UsuarioAutenticado usuarioAutenticado) {
        permiteApenasAdmin(usuarioAutenticado);
        return repository.findByTipo(tipoUsuario.toString()).stream().map(mapper::paraResponse).toList();
    }

    public UsuarioResponse cadastrar(UsuarioCadastroRequest request, UsuarioAutenticado usuarioAutenticado) {
        permiteApenasAdmin(usuarioAutenticado);
        var existenteComMesmoEmail = buscarPorEmail(request.email());

        if (existenteComMesmoEmail.isPresent()) {
            throw new UsuarioDadosInvalidosException("E-mail não esta disponível para cadastro.");
        }

        Usuario usuario = Usuario.criar(
                request.nome(),
                new Email(request.email()),
                codificadorSenha.codificar(request.senha()),
                TipoUsuario.valueOf(request.tipo())
        );

        UsuarioModel model = mapper.paraModelo(usuario);
        return mapper.paraResponse(repository.save(model));
    }

    @Transactional
    public UsuarioResponse atualizar(UUID usuarioId, UsuarioEdicaoRequest request, UsuarioAutenticado usuarioAutenticado) {
        permiteOProprioOuAdmin(usuarioAutenticado, usuarioId);
        UsuarioModel usuarioEncontrado = encontrarPorId(usuarioId);
        Optional<UsuarioModel> existenteComMesmoEmail = buscarPorEmail(request.email());

        if (existenteComMesmoEmail.isPresent() && !existenteComMesmoEmail.get().getId().equals(usuarioId)) {
            throw new UsuarioDadosInvalidosException("E-mail não esta disponível para cadastro.");
        }
        Usuario usuarioParaAlterar = mapper.paraEntidade(usuarioEncontrado);

        usuarioParaAlterar.alterarDados(request.nome(), new Email(request.email()));

        mapper.atualizarModelo(usuarioParaAlterar, usuarioEncontrado);
        return mapper.paraResponse(repository.save(usuarioEncontrado));
    }

    @Transactional
    public void ativarUsuario(UUID usuarioId, UsuarioAutenticado usuarioAutenticado) {
        permiteApenasAdmin(usuarioAutenticado);
        UsuarioModel usuarioEncontrado = encontrarPorId(usuarioId);
        Usuario usuario = mapper.paraEntidade(usuarioEncontrado);
        usuario.ativar();
        mapper.atualizarModelo(usuario, usuarioEncontrado);
        repository.save(usuarioEncontrado);
    }

    @Transactional
    public void inativarUsuario(UUID usuarioId, UsuarioAutenticado usuarioAutenticado) {
        permiteApenasAdmin(usuarioAutenticado);
        UsuarioModel usuarioEncontrado = encontrarPorId(usuarioId);
        Usuario usuario = mapper.paraEntidade(usuarioEncontrado);
        usuario.inativar();
        mapper.atualizarModelo(usuario, usuarioEncontrado);
        repository.save(usuarioEncontrado);
    }

    @Transactional
    public void excluirUsuario(UUID usuarioId, UsuarioAutenticado usuarioAutenticado) {
        permiteApenasAdmin(usuarioAutenticado);
        UsuarioModel usuarioEncontrado = encontrarPorId(usuarioId);
        Usuario usuario = mapper.paraEntidade(usuarioEncontrado);
        usuario.excluir();
        mapper.atualizarModelo(usuario, usuarioEncontrado);
        repository.save(usuarioEncontrado);
    }

    @Transactional
    public void alterarSenhaUsuario(UUID usuarioId, AlteracaoSenha alteracaoSenha, UsuarioAutenticado usuarioAutenticado) {
        permiteOProprioOuAdmin(usuarioAutenticado, usuarioId);
        UsuarioModel usuarioEncontrado = encontrarPorId(usuarioId);
        Usuario usuario = mapper.paraEntidade(usuarioEncontrado);
        usuario.definirNovaSenha(codificadorSenha.codificar(alteracaoSenha.senhaNova()));
        mapper.atualizarModelo(usuario, usuarioEncontrado);
        repository.save(usuarioEncontrado);
    }

    private void permiteOProprioOuAdmin(UsuarioAutenticado usuarioAutenticado, UUID alvoUuid) {
        if (usuarioAutenticado.tipo() != TipoUsuario.ADMINISTRADOR && !Objects.equals(usuarioAutenticado.uuid(), alvoUuid)) {
            throw new UsuarioNaoAutorizadoException();
        }
    }

    private void permiteApenasAdmin(UsuarioAutenticado usuarioAutenticado) {
        PermissaoValidator.verificarPermissao(usuarioAutenticado.tipo(), Set.of(TipoUsuario.ADMINISTRADOR));
    }

    private UsuarioModel encontrarPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException());
    }

    private Optional<UsuarioModel> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}
