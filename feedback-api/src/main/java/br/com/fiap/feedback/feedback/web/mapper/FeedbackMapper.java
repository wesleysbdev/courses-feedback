package br.com.fiap.feedback.feedback.web.mapper;

import br.com.fiap.feedback.feedback.domain.entity.Feedback;
import br.com.fiap.feedback.feedback.domain.vo.FeedbackAvaliacao;
import br.com.fiap.feedback.feedback.persistence.model.FeedbackModel;
import br.com.fiap.feedback.feedback.web.dto.FeedbackResponse;
import br.com.fiap.feedback.infraestructure.mapper.VoEmailMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VoEmailMapper.class})
public interface FeedbackMapper {
    @Mapping(target = "matriculaId", source = "matricula.id")
    @Mapping(target = "avaliacao", source = "nota")
    FeedbackResponse paraResponse(FeedbackModel feedbackModel);

    Feedback paraEntidade(FeedbackModel feedbackModel);

    @Mapping(target = "nota", source = "avaliacao.nota")
    FeedbackModel paraModelo(Feedback feedback);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nota", source = "feedback.avaliacao.nota")
    void atualizarModelo(Feedback feedback, @MappingTarget FeedbackModel feedbackModel);

    default FeedbackAvaliacao map(int nota) {
        return new FeedbackAvaliacao(nota);
    }
}
