package br.com.fiap.feedback.feedback.service;

import br.com.fiap.feedback.autenticacao.domain.dto.UsuarioAutenticado;
import br.com.fiap.feedback.feedback.domain.entity.Feedback;
import br.com.fiap.feedback.feedback.domain.exception.FeedbackDadosInvalidosException;
import br.com.fiap.feedback.feedback.domain.exception.FeedbackNaoEncontradoException;
import br.com.fiap.feedback.feedback.persistence.model.FeedbackModel;
import br.com.fiap.feedback.feedback.persistence.repository.FeedbackRepository;
import br.com.fiap.feedback.feedback.web.dto.FeedbackRequest;
import br.com.fiap.feedback.feedback.web.dto.FeedbackResponse;
import br.com.fiap.feedback.feedback.web.mapper.FeedbackMapper;
import br.com.fiap.feedback.matricula.domain.exception.MatriculaNaoEncontradaException;
import br.com.fiap.feedback.matricula.persistence.model.MatriculaModel;
import br.com.fiap.feedback.matricula.persistence.repository.MatriculaRepository;
import br.com.fiap.feedback.matricula.web.mapper.MatriculaMapper;
import br.com.fiap.feedback.shared.PermissaoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository repository;
    private final FeedbackMapper mapper;
    private final MatriculaRepository matriculaRepository;
    private final MatriculaMapper matriculaMapper;

    @Transactional
    public FeedbackResponse criar(FeedbackRequest request, UsuarioAutenticado usuarioAutenticado) {
        PermissaoValidator.permiteApenasAluno(usuarioAutenticado);

        MatriculaModel matriculaModel = matriculaRepository.findById(request.matriculaId())
                .orElseThrow(() -> new MatriculaNaoEncontradaException());

        if (!matriculaModel.getAluno().getId().equals(usuarioAutenticado.id())) {
            throw new FeedbackDadosInvalidosException("Apenas o aluno matriculado pode fazer um feedback relacionado ao curso.");
        }

        Feedback feedback = Feedback.criar(matriculaMapper.paraEntidade(matriculaModel), request.descricao(), request.nota());
        FeedbackModel model = mapper.paraModelo(feedback);

        return mapper.paraResponse(repository.save(model));
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> listarTodos() {
        return repository.findAll().stream().map(mapper::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public FeedbackResponse buscarPorId(UUID cursoId) {
        return mapper.paraResponse(encontrarPorId(cursoId));
    }

    private FeedbackModel encontrarPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new FeedbackNaoEncontradoException());
    }
}
