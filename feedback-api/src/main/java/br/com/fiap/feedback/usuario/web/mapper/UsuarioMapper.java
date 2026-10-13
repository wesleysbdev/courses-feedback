package br.com.fiap.feedback.usuario.web.mapper;

import br.com.fiap.feedback.usuario.domain.entity.Usuario;
import br.com.fiap.feedback.usuario.domain.vo.Email;
import br.com.fiap.feedback.usuario.persistence.model.UsuarioModel;
import br.com.fiap.feedback.usuario.web.dto.UsuarioCadastroRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioEdicaoRequest;
import br.com.fiap.feedback.usuario.web.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponse paraResponse(UsuarioModel usuarioModel);

    Usuario paraEntidade(UsuarioModel usuarioModel);

    UsuarioModel paraModelo(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    void atualizarModelo(Usuario usuario, @MappingTarget UsuarioModel existente);

    default Email paraEmail(String valor) {
        return valor == null ? null : new Email(valor);
    }

    default String paraString(Email email) {
        return email == null ? null : email.valor();
    }
}
