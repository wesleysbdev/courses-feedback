package br.com.fiap.feedback.autenticacao.web.controller;

import br.com.fiap.feedback.autenticacao.domain.dto.Autenticacao;
import br.com.fiap.feedback.autenticacao.service.AutenticacaoService;
import br.com.fiap.feedback.autenticacao.web.dto.AutenticacaoRequest;
import br.com.fiap.feedback.autenticacao.web.dto.AutenticacaoResponse;
import br.com.fiap.feedback.autenticacao.web.mapper.AutenticacaoMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("autenticacao")
@AllArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;
    private final AutenticacaoMapper mapper;

    @PostMapping
    public AutenticacaoResponse autenticar(@RequestBody @Valid AutenticacaoRequest request) {
        Autenticacao dto = mapper.paraDto(request);
        var token = autenticacaoService.autenticar(dto);
        return new AutenticacaoResponse(token);
    }

}
