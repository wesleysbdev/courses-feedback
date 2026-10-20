package br.com.fiap.feedback.usuario.persistence.repository;

import br.com.fiap.feedback.usuario.domain.enums.TipoUsuario;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    @Query(value = "SELECT * FROM usuario WHERE tipo = :tipoUsuario", nativeQuery = true)
    List<UsuarioModel> findByTipo(String tipoUsuario);

    Optional<UsuarioModel> findByIdAndTipo(UUID uuid, TipoUsuario tipoUsuario);

    Optional<UsuarioModel> findByEmail(String valor);

    List<UsuarioModel> findByExcluidoFalse();
}
