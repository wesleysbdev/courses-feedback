package br.com.fiap.feedback.feedback.persistence.repository;

import br.com.fiap.feedback.feedback.persistence.model.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<FeedbackModel, UUID> {
}
