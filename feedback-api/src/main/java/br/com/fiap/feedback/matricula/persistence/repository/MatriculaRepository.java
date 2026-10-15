package br.com.fiap.feedback.matricula.persistence.repository;

import br.com.fiap.feedback.matricula.domain.enums.StatusMatricula;
import br.com.fiap.feedback.matricula.persistence.model.MatriculaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, UUID> {

    List<MatriculaModel> findByAlunoId(UUID alunoId);

    List<MatriculaModel> findByCursoId(UUID cursoId);

    boolean existsByAlunoIdAndCursoId(
            UUID alunoId,
            UUID cursoId
    );

    boolean existsByAlunoIdAndCursoIdAndStatus(
            UUID alunoId,
            UUID cursoId,
            StatusMatricula status
    );
}
