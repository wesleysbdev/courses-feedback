package br.com.fiap.feedback.feedback.persistence.model;

import br.com.fiap.feedback.matricula.persistence.model.MatriculaModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedback")
@Getter
@Setter
public class FeedbackModel {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "matricula_id", nullable = false, unique = true)
    private MatriculaModel matricula;

    @Column(nullable = false, length = 1024)
    private String descricao;

    @Column(nullable = false)
    private int nota;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

}
