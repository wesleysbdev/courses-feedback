package br.com.fiap.feedback.matricula.web.mapper;

import br.com.fiap.feedback.infraestructure.mapper.VoEmailMapper;
import br.com.fiap.feedback.matricula.domain.entity.Matricula;
import br.com.fiap.feedback.matricula.persistence.model.MatriculaModel;
import br.com.fiap.feedback.matricula.web.dto.MatriculaResponse;
import br.com.fiap.feedback.usuario.web.mapper.UsuarioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, VoEmailMapper.class})
public interface MatriculaMapper {
    MatriculaResponse paraResponse(MatriculaModel matriculaModel);

    Matricula paraEntidade(MatriculaModel matriculaModel);

    MatriculaModel paraModelo(Matricula matricula);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matriculadoEm", ignore = true)
    void atualizarModelo(Matricula matricula, @MappingTarget MatriculaModel matriculaModel);
}
