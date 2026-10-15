package br.com.fiap.feedback.curso.persistence.repository;

import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CursoRepository extends JpaRepository<CursoModel, UUID> {

    List<CursoModel> findByAtivoTrue();

    List<CursoModel> findByProfessorId(UUID professorId);
}