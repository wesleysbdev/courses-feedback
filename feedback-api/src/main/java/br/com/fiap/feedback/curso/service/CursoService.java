package br.com.fiap.feedback.curso.service;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.autenticacao.domain.exception.UsuarioNaoAutorizadoException;
import br.com.fiap.feedback.curso.domain.entity.Curso;
import br.com.fiap.feedback.curso.domain.exception.CursoNaoEncontradoException;
import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import br.com.fiap.feedback.curso.persistence.repository.CursoRepository;
import br.com.fiap.feedback.curso.web.dto.CursoRequest;
import br.com.fiap.feedback.curso.web.dto.CursoResponse;
import br.com.fiap.feedback.curso.web.mapper.CursoMapper;
import br.com.fiap.feedback.shared.PermissaoValidator;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioNaoEncontradoException;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.persistence.repository.UsuarioRepository;
import br.com.fiap.feedback.usuario.web.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final CursoMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public CursoResponse cadastrar(CursoRequest request, UsuarioAutenticado usuarioAutenticado) {
        PermissaoValidator.permiteApenasAdmin(usuarioAutenticado);

        UsuarioModel professor = usuarioRepository.findByIdAndTipo(request.professorId(), TipoUsuario.PROFESSOR)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Professor não encontrado."));

        if (!professor.isAtivo() || professor.isExcluido()) {
            throw new UsuarioNaoAutorizadoException("O professor não está disponível.");
        }

        Curso curso = Curso.criar(
                request.nome(),
                request.descricao(),
                usuarioMapper.paraEntidade(professor)
        );

        CursoModel model = mapper.paraModelo(curso);

        return mapper.paraResponse(repository.save(model));
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> listarTodos() {
        List<CursoModel> models = repository.findByAtivoTrue();
        return models.stream().map(mapper::paraResponse).toList();
    }
    
    @Transactional(readOnly = true)
    public CursoResponse buscarPorId(UUID cursoId) {
        return mapper.paraResponse(encontrarPorId(cursoId));
    }

    private CursoModel encontrarPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CursoNaoEncontradoException());
    }
}
