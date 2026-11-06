package br.com.fiap.report.controller;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/teste/redis")
@Produces(MediaType.TEXT_PLAIN)
public class RedisTesteController {

    private final ValueCommands<String, String> comandos;

    @Inject
    public RedisTesteController(RedisDataSource redisDataSource) {
        this.comandos = redisDataSource.value(String.class);
    }

    @GET
    public String testarConexao() {
        comandos.set("teste:conexao", "Redis conectado com sucesso!");
        return comandos.get("teste:conexao");
    }
}
