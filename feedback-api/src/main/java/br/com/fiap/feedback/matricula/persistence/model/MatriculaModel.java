package br.com.fiap.feedback.matricula.persistence.model;

import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import br.com.fiap.feedback.matricula.domain.enums.StatusMatricula;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "matricula",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_matricula_aluno_curso",
                        columnNames = {"aluno_id", "curso_id"}
                )
        }
)
@Getter
@Setter
public class MatriculaModel {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private UsuarioModel aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private CursoModel curso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMatricula status;

    @CreationTimestamp
    @Column(name = "matriculado_em", nullable = false, updatable = false)
    private LocalDateTime matriculadoEm;

}
