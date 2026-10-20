package br.com.fiap.feedback.matricula.web.controller;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.infraestructure.security.SecurityContextProvider;
import br.com.fiap.feedback.matricula.service.MatriculaService;
import br.com.fiap.feedback.matricula.web.dto.MatriculaRequest;
import br.com.fiap.feedback.matricula.web.dto.MatriculaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService service;
    private final SecurityContextProvider contextProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponse cadastrarMatricula(@RequestBody @Valid MatriculaRequest request) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.matricular(usuarioAutenticado.id(), request.cursoId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MatriculaResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MatriculaResponse buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }
}
