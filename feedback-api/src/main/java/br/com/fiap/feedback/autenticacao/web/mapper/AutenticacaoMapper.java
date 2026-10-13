package br.com.fiap.feedback.autenticacao.web.mapper;

import br.com.fiap.feedback.autenticacao.domain.dto.Autenticacao;
import br.com.fiap.feedback.autenticacao.web.dto.AutenticacaoRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutenticacaoMapper {
    Autenticacao paraDto(AutenticacaoRequest request);
}
