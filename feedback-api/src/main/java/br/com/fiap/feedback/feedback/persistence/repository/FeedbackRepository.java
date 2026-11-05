package br.com.fiap.feedback.feedback.persistence.repository;

import br.com.fiap.feedback.feedback.persistence.model.FeedbackModel;
import br.com.fiap.feedback.feedback.persistence.projection.ResumoFeedbackDiarioProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<FeedbackModel, UUID> {

    @Query("""
            SELECT new br.com.fiap.feedback.feedback.persistence.projection.ResumoFeedbackDiarioProjection(
                :data,
                COALESCE(SUM(f.nota), 0),
                COUNT(f.id),
                COALESCE(SUM(CASE WHEN f.nota <= 4 THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN f.nota BETWEEN 5 AND 7 THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN f.nota >= 8 THEN 1 ELSE 0 END), 0)
            )
            FROM FeedbackModel f
            WHERE f.criadoEm >= :inicio
              AND f.criadoEm < :fim
            """)
    ResumoFeedbackDiarioProjection buscarResumoDiario(
            @Param("data") java.time.LocalDate data,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

}
