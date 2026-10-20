package br.com.fiap.feedback.matricula.service;

import br.com.fiap.feedback.autenticacao.domain.exception.UsuarioNaoAutorizadoException;
import br.com.fiap.feedback.curso.domain.exception.CursoNaoEncontradoException;
import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import br.com.fiap.feedback.curso.persistence.repository.CursoRepository;
import br.com.fiap.feedback.curso.web.mapper.CursoMapper;
import br.com.fiap.feedback.matricula.domain.entity.Matricula;
import br.com.fiap.feedback.matricula.domain.exception.MatriculaDadosInvalidosException;
import br.com.fiap.feedback.matricula.domain.exception.MatriculaNaoEncontradaException;
import br.com.fiap.feedback.matricula.persistence.model.MatriculaModel;
import br.com.fiap.feedback.matricula.persistence.repository.MatriculaRepository;
import br.com.fiap.feedback.matricula.web.dto.MatriculaResponse;
import br.com.fiap.feedback.matricula.web.mapper.MatriculaMapper;
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
public class MatriculaService {

    private final MatriculaRepository repository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatriculaMapper mapper;
    private final UsuarioMapper usuarioMapper;
    private final CursoMapper cursoMapper;

    @Transactional
    public MatriculaResponse matricular(UUID alunoId, UUID cursoId) {

        UsuarioModel aluno = usuarioRepository.findByIdAndTipo(alunoId, TipoUsuario.ALUNO)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Aluno não encontrado."));

        if (!aluno.isAtivo() || aluno.isExcluido()) {
            throw new UsuarioNaoAutorizadoException("O aluno não está disponível.");
        }

        CursoModel curso = cursoRepository.findByIdAndAtivoTrue(cursoId)
                .orElseThrow(() -> new CursoNaoEncontradoException());

        boolean jaMatriculado = repository.existsByAlunoIdAndCursoId(alunoId, cursoId);

        if (jaMatriculado) {
            throw new MatriculaDadosInvalidosException("Já existe uma matrícula para esse aluno e curso.");
        }

        Matricula matricula = Matricula.criar(usuarioMapper.paraEntidade(aluno), cursoMapper.paraEntidade(curso));
        MatriculaModel model = mapper.paraModelo(matricula);

        return mapper.paraResponse(repository.save(model));
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponse> listarTodos() {
        return repository.findAll().stream().map(mapper::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public MatriculaResponse buscarPorId(UUID cursoId) {
        return mapper.paraResponse(encontrarPorId(cursoId));
    }

    private MatriculaModel encontrarPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new MatriculaNaoEncontradaException());
    }
}