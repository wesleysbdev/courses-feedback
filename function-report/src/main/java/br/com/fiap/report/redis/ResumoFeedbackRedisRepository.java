package br.com.fiap.report.redis;

import br.com.fiap.report.dto.response.ResumoFeedbackDiarioResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class ResumoFeedbackRedisRepository {

    private static final String PREFIXO_CHAVE = "feedback:resumo:diario:";
    private final ValueCommands<String, String> comandos;
    private final ObjectMapper objectMapper;

    @Inject
    public ResumoFeedbackRedisRepository(RedisDataSource redisDataSource, ObjectMapper objectMapper) {
        this.comandos = redisDataSource.value(String.class);
        this.objectMapper = objectMapper;
    }

    public void salvar(ResumoFeedbackDiarioResponse resumo) {
        try {
            String chave = gerarChave(resumo.data());
            String valor = objectMapper.writeValueAsString(resumo);

            comandos.set(chave, valor);

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Não foi possível serializar o resumo diário.", e);
        }
    }

    public Optional<ResumoFeedbackDiarioResponse> buscarPorData(LocalDate data) {

        String chave = gerarChave(data);
        String valor = comandos.get(chave);

        if (valor == null) {
            return Optional.empty();
        }

        try {
            ResumoFeedbackDiarioResponse resumo = objectMapper.readValue(valor, ResumoFeedbackDiarioResponse.class);

            return Optional.of(resumo);

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Não foi possível desserializar o resumo diário.", e);
        }
    }

    private String gerarChave(LocalDate data) {
        return PREFIXO_CHAVE + data;
    }
}
