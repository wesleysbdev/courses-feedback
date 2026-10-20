package br.com.fiap.feedback.feedback.web.controller;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.feedback.service.FeedbackService;
import br.com.fiap.feedback.feedback.web.dto.FeedbackRequest;
import br.com.fiap.feedback.feedback.web.dto.FeedbackResponse;
import br.com.fiap.feedback.infraestructure.security.SecurityContextProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;
    private final SecurityContextProvider contextProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse cadastrarFeedback(@RequestBody @Valid FeedbackRequest request) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.criar(request, usuarioAutenticado);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FeedbackResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FeedbackResponse buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }
}
