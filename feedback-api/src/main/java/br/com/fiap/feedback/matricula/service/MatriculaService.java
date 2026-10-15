package br.com.fiap.feedback.matricula.service;

import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import br.com.fiap.feedback.curso.persistence.repository.CursoRepository;
import br.com.fiap.feedback.matricula.domain.enums.StatusMatricula;
import br.com.fiap.feedback.matricula.persistence.model.MatriculaModel;
import br.com.fiap.feedback.matricula.persistence.repository.MatriculaRepository;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import br.com.fiap.feedback.usuario.domain.exception.UsuarioNaoEncontradoException;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.persistence.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MatriculaModel matricular(UUID alunoId, UUID cursoId) {

        // TO DO: Ajustar exception
        UsuarioModel aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Aluno não encontrado."));

        // TO DO: Ajustar exception
        if (aluno.getTipo() != TipoUsuario.ALUNO) {
            throw new IllegalArgumentException("O usuário informado não é um aluno.");
        }

        // TO DO: Ajustar exception
        if (!aluno.isAtivo() || aluno.isExcluido()) {
            throw new IllegalArgumentException("O aluno não está disponível.");
        }

        // TO DO: Ajustar exception
        CursoModel curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado."));

        // TO DO: Ajustar exception
        if (!curso.isAtivo()) {
            throw new IllegalArgumentException("O curso não está disponível para matrícula.");
        }


        boolean jaMatriculado = matriculaRepository.existsByAlunoIdAndCursoId(alunoId, cursoId);

        // TO DO: Ajustar exception
        if (jaMatriculado) {
            throw new IllegalArgumentException("Já existe uma matrícula para esse aluno e curso.");
        }

        // TO DO: Ajustar para domínio
        MatriculaModel matricula = new MatriculaModel();

        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setStatus(StatusMatricula.EM_ANDAMENTO);

        return matriculaRepository.save(matricula);
    }
}