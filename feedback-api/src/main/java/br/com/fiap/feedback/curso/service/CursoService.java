package br.com.fiap.feedback.curso.service;

import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import br.com.fiap.feedback.curso.persistence.repository.CursoRepository;
import br.com.fiap.feedback.curso.web.dto.CursoRequest;
import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.persistence.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CursoModel criar(CursoRequest request) {

        // TO DO: Ajustar exception
        UsuarioModel professor = usuarioRepository.findById(request.professorId())
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado."));

        // TO DO: Ajustar exception
        if (professor.getTipo() != TipoUsuario.PROFESSOR) {
            throw new IllegalArgumentException("O usuário informado não é um professor.");
        }

        // TO DO: Ajustar exception
        if (!professor.isAtivo() || professor.isExcluido()) {
            throw new IllegalArgumentException("O professor não está disponível.");
        }

        // TO DO: Ajustar para domínio

        CursoModel curso = new CursoModel();

        curso.setNome(request.nome());
        curso.setDescricao(request.descricao());
        curso.setProfessor(professor);
        curso.setAtivo(true);

        return cursoRepository.save(curso);
    }
}
