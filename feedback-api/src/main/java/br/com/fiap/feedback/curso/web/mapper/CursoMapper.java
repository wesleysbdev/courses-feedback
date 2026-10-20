package br.com.fiap.feedback.curso.web.mapper;

import br.com.fiap.feedback.curso.domain.entity.Curso;
import br.com.fiap.feedback.curso.persistence.model.CursoModel;
import br.com.fiap.feedback.curso.web.dto.CursoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CursoMapper {
    CursoResponse paraResponse(CursoModel cursoModel);

    Curso paraEntidade(CursoModel cursoModel);

    CursoModel paraModelo(Curso curso);

    @Mapping(target = "id", ignore = true)
    void atualizarModelo(Curso curso, @MappingTarget CursoModel cursoModel);
}
