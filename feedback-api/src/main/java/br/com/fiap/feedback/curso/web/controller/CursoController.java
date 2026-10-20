package br.com.fiap.feedback.curso.web.controller;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.curso.service.CursoService;
import br.com.fiap.feedback.curso.web.dto.CursoRequest;
import br.com.fiap.feedback.curso.web.dto.CursoResponse;
import br.com.fiap.feedback.infraestructure.security.SecurityContextProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService service;
    private final SecurityContextProvider contextProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponse cadastrarCurso(@RequestBody @Valid CursoRequest request) {
        UsuarioAutenticado usuarioAutenticado = contextProvider.obterUsuarioAutenticado();
        return service.cadastrar(request, usuarioAutenticado);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CursoResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CursoResponse buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }
}
