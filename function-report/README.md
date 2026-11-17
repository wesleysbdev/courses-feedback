# Function Report

## 1. Visão geral

A `function-report` é o componente responsável pela consolidação diária dos dados de feedback e pela geração de relatórios gerenciais por período.

Ela não acessa diretamente o banco de dados da aplicação. Em vez disso, utiliza a `feedback-api` como fonte dos dados agregados e armazena os resultados diários no Redis. Quando um relatório é solicitado, a function recupera os resumos diários correspondentes ao intervalo informado e calcula os indicadores consolidados.

As principais responsabilidades são:

- solicitar à `feedback-api` os agregados de feedback de uma data;
- persistir os resumos diários no Redis;
- executar automaticamente a consolidação do dia anterior;
- gerar relatórios por intervalo de datas;
- validar o período solicitado e a existência dos resumos necessários;
- proteger o endpoint de relatório por autenticação entre serviços.

## 2. Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| Java 25 | Linguagem e plataforma de execução |
| Quarkus 3.40.1 | Framework da aplicação |
| Maven | Gerenciamento de dependências e build |
| Quarkus REST | Exposição dos endpoints HTTP |
| Quarkus REST Client | Comunicação HTTP com a `feedback-api` |
| Jackson | Serialização e desserialização dos dados JSON |
| Redis | Armazenamento dos resumos agregados por dia |
| Quarkus Scheduler | Agendamento da consolidação diária na execução atual |
| Docker | Empacotamento e execução em container |

## 3. Estrutura do projeto

```text
function-report/
├── pom.xml
├── Dockerfile
└── src/
    └── main/
        ├── java/
        │   └── br/com/fiap/report/
        │       ├── client/
        │       │   ├── FeedbackApiClient.java
        │       │   └── InternalServiceAuthFilter.java
        │       ├── controller/
        │       │   ├── RelatorioController.java
        │       │   ├── FeedbackApiTesteController.java
        │       │   ├── RedisTesteController.java
        │       │   └── ConsolidacaoTesteController.java
        │       ├── dto/
        │       │   └── response/
        │       ├── exception/
        │       ├── redis/
        │       │   └── ResumoFeedbackRedisRepository.java
        │       ├── scheduler/
        │       │   └── ConsolidacaoDiariaScheduler.java
        │       ├── security/
        │       │   └── InternalServiceAuthenticationFilter.java
        │       ├── service/
        │       │   ├── ConsolidacaoDiariaService.java
        │       │   └── RelatorioService.java
        │       └── Main.java
        └── resources/
            └── application.yml
```

### Responsabilidade dos principais componentes

- **`FeedbackApiClient`:** declara a chamada HTTP para consultar o resumo de feedback de uma data na API.
- **`InternalServiceAuthFilter`:** inclui o cabeçalho `X-Internal-Service-Token` nas requisições enviadas à `feedback-api`.
- **`ConsolidacaoDiariaService`:** coordena a consulta do resumo diário na API e sua gravação no Redis.
- **`ResumoFeedbackRedisRepository`:** encapsula a leitura e a escrita dos resumos diários no Redis.
- **`ConsolidacaoDiariaScheduler`:** agenda a consolidação automática do dia anterior.
- **`RelatorioService`:** valida o período solicitado, recupera os resumos diários e calcula os indicadores consolidados.
- **`RelatorioController`:** expõe o endpoint de geração de relatório.
- **`InternalServiceAuthenticationFilter`:** valida o token recebido nas chamadas ao endpoint de relatório.
- **`exception`:** contém os mapeadores que transformam exceções em respostas HTTP padronizadas.

## 4. Integração com a feedback-api

A `function-report` utiliza a `feedback-api` como fonte oficial dos dados. Para isso, consome o endpoint interno:

```http
GET /api/v1/internal/feedbacks/agregados-diarios/{data}
X-Internal-Service-Token: <token-interno>
```

A data é informada no formato `yyyy-MM-dd`.

O endpoint retorna um resumo diário com os seguintes campos:

| Campo | Descrição |
|---|---|
| `data` | Data à qual o resumo se refere |
| `somaNotas` | Soma das notas dos feedbacks do dia |
| `totalAvaliacoes` | Quantidade total de feedbacks |
| `avaliacoesCriticas` | Quantidade de avaliações críticas |
| `avaliacoesNormais` | Quantidade de avaliações normais |
| `avaliacoesBoas` | Quantidade de avaliações boas |

A chamada é declarada em `FeedbackApiClient` e utiliza o `InternalServiceAuthFilter` para enviar o token de autenticação interna.

A URL-base da API é configurada por `FEEDBACK_API_URL`. No Docker Compose, a comunicação ocorre pela rede interna dos containers; fora desse ambiente, o endereço deve apontar para a instância acessível da API.

## 5. Processo de consolidação diária

A consolidação transforma os dados agregados disponibilizados pela API em resumos diários persistidos no Redis.

O fluxo executado por `ConsolidacaoDiariaService` é:

1. Recebe a data que deve ser consolidada.
2. Consulta a `feedback-api` por meio de `FeedbackApiClient`.
3. Recebe o resumo agregado da data.
4. Envia o resumo para `ResumoFeedbackRedisRepository`.
5. O repositório serializa o objeto em JSON e grava o valor no Redis.

A chave utilizada no Redis segue o padrão:

```text
feedback:resumo:diario:yyyy-MM-dd
```

Exemplo:

```text
feedback:resumo:diario:2026-11-14
```

O valor armazenado é o JSON correspondente ao `ResumoFeedbackDiarioResponse`, contendo a data, a soma das notas, o total de avaliações e as quantidades por classificação.

A gravação utiliza o comando `SET` do Redis. Portanto, uma nova consolidação da mesma data substitui o resumo anteriormente armazenado. Isso permite reprocessar um dia e atualizar seu agregado a partir dos dados atuais da API.

### Agendamento automático

A consolidação automática é executada por `ConsolidacaoDiariaScheduler`, utilizando o Quarkus Scheduler.

A configuração atual é:

```java
@Scheduled(cron = "0 10 0 * * ?", timeZone = "America/Sao_Paulo")
```

O processo é executado diariamente às **00h10**, considerando o fuso horário `America/Sao_Paulo`, e consolida a data do dia anterior.

A data é calculada com:

```java
LocalDate.now(ZoneId.of("America/Sao_Paulo")).minusDays(1);
```

Se ocorrer uma falha durante a consolidação, o erro é registrado nos logs e relançado pelo scheduler.

> **Observação sobre cloud:** o código deste módulo utiliza atualmente o Quarkus Scheduler, ou seja, o agendamento está dentro da própria aplicação. Caso a implantação utilize um agendador gerenciado da cloud, como o Amazon EventBridge Scheduler, será necessário configurar esse acionamento e adaptar a execução para o modelo escolhido. Essa integração externa não está configurada neste módulo.

## 6. Persistência dos resumos no Redis

O Redis é utilizado como armazenamento dos agregados diários que serão consultados durante a geração dos relatórios.

A aplicação utiliza a API `RedisDataSource` do Quarkus e os comandos de valor (`ValueCommands<String, String>`).

O repositório implementa duas operações principais:

- **Salvar:** converte `ResumoFeedbackDiarioResponse` para JSON e grava o conteúdo na chave referente à data.
- **Buscar:** consulta a chave da data e, quando existe um valor, converte o JSON novamente para `ResumoFeedbackDiarioResponse`.

Quando não existe uma chave para a data consultada, o repositório retorna `Optional.empty()`. O tratamento dessa ausência é realizado pelo serviço de relatório.

A function não mantém tabelas próprias nem acessa diretamente o PostgreSQL. O Redis contém somente os resumos diários necessários à montagem dos relatórios.

## 7. Geração de relatórios

O endpoint principal da aplicação é:

```http
GET /feedbacks/relatorio/inicio/{inicio}/fim/{fim}
```

Exemplo:

```http
GET /feedbacks/relatorio/inicio/2026-11-01/fim/2026-11-14
X-Internal-Service-Token: <token-interno>
```

As datas devem utilizar o formato `yyyy-MM-dd`.

O fluxo executado por `RelatorioService` é:

1. Validar as datas inicial e final.
2. Percorrer todos os dias do intervalo, incluindo as datas inicial e final.
3. Buscar no Redis o resumo diário de cada data.
4. Somar as notas, o total de avaliações e as quantidades de cada classificação.
5. Registrar a quantidade de avaliações de cada dia em `quantidadePorDia`.
6. Calcular a média geral das notas.
7. Retornar o relatório consolidado.

### Regras de validação do período

O serviço aplica as seguintes regras:

- as datas inicial e final são obrigatórias;
- a data inicial não pode ser posterior à data final;
- a data final não pode ser posterior ao dia anterior, considerando `America/Sao_Paulo`;
- o período solicitado não pode ultrapassar o limite configurado em `app.security.maximo-dias-relatorio`;
- todos os dias do intervalo precisam possuir um resumo previamente consolidado no Redis.

O limite atual é de **30 dias**, configurado no `application.yml`. O cálculo considera as datas inicial e final, portanto um intervalo de 1º a 30 de um mês corresponde a 30 dias.

Quando não existe resumo para algum dos dias solicitados, a geração do relatório é interrompida e a aplicação retorna `503 Service Unavailable`, informando a data sem consolidação. A function não realiza automaticamente a consolidação de dias ausentes durante a consulta do relatório.

### Cálculo da média

A média é calculada pela divisão da soma das notas pelo total de avaliações:

```text
média = somaNotas / totalAvaliacoes
```

O resultado é arredondado para uma casa decimal, utilizando `RoundingMode.HALF_UP`.

Quando o período não possui avaliações, `mediaNotas` é retornado como `null`, enquanto os totais e as quantidades por classificação permanecem zerados.

### Exemplo de resposta

```json
{
  "periodo": {
    "inicio": "2026-11-01",
    "fim": "2026-11-03"
  },
  "mediaNotas": 6.3,
  "totalAvaliacoes": 3,
  "avaliacoesCriticas": 1,
  "avaliacoesNormais": 1,
  "avaliacoesBoas": 1,
  "quantidadePorDia": {
    "2026-11-01": 0,
    "2026-11-02": 0,
    "2026-11-03": 3
  }
}
```

Os números acima são ilustrativos. A resposta real depende dos resumos armazenados no Redis para as datas solicitadas.

## 8. Segurança e comunicação entre serviços

A comunicação interna utiliza o cabeçalho:

```http
X-Internal-Service-Token: <token-interno>
```

O mesmo segredo deve estar configurado na `feedback-api` e na `function-report`.

Existem dois pontos de aplicação dessa autenticação:

**Requisições enviadas à feedback-api**

O `InternalServiceAuthFilter` da camada `client` adiciona automaticamente o cabeçalho às chamadas feitas por `FeedbackApiClient`. Assim, o token não precisa ser informado individualmente em cada método de integração.

**Requisições recebidas para geração de relatório**

O `InternalServiceAuthenticationFilter`, registrado como provider JAX-RS, verifica o cabeçalho nas requisições destinadas ao caminho `/feedbacks/relatorio/`.

A comparação do token é realizada com `MessageDigest.isEqual`. Quando o cabeçalho está ausente ou o valor é inválido, a requisição é interrompida e a aplicação retorna `401 Unauthorized`, com uma resposta JSON no formato:

```json
{
  "status": 401,
  "erro": "Unauthorized",
  "mensagem": "Autenticação interna inválida."
}
```

O token é obtido pela propriedade `app.security.internal-token`, configurada por `INTERNAL_SERVICE_TOKEN`.

> **Cuidados para produção:** configure um segredo forte, exclusivo e compartilhado somente entre os serviços autorizados. Não utilize o valor simbólico definido para execução local e não versione segredos reais no repositório.

## 9. Endpoints disponíveis

### Endpoint principal

| Método | Caminho | Finalidade | Autenticação |
|---|---|---|---|
| `GET` | `/feedbacks/relatorio/inicio/{inicio}/fim/{fim}` | Gera relatório consolidado para o período informado | Token interno |

### Endpoints de apoio e demonstração

O projeto também contém endpoints com o prefixo `/teste`, utilizados para verificar separadamente as integrações e facilitar a demonstração do funcionamento do módulo.

| Método | Caminho | Finalidade |
|---|---|---|
| `GET` | `/teste/feedback-api/{data}` | Consulta diretamente o resumo diário na `feedback-api` |
| `GET` | `/teste/redis` | Grava e recupera um valor de teste no Redis |
| `GET` | `/teste/consolidacao/{data}` | Executa manualmente a consolidação de uma data |

Esses endpoints não fazem parte da interface principal de relatórios. No filtro de autenticação atual, a validação do token é aplicada somente ao caminho do relatório; os caminhos `/teste/...` não passam por essa validação.

> **Atenção:** os endpoints de teste são úteis no ambiente local e na demonstração acadêmica, mas devem ser protegidos, restringidos ou removidos antes de disponibilizar a aplicação em um ambiente de produção.

## 10. Tratamento de erros

A aplicação utiliza `ExceptionMapper` para padronizar as respostas de erro no formato:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Descrição do erro."
}
```

Os principais cenários tratados são:

| HTTP | Situação |
|---|---|
| `400 Bad Request` | Datas ausentes, período invertido, data final posterior ao dia anterior ou intervalo maior que o limite configurado |
| `401 Unauthorized` | Token interno ausente ou inválido no endpoint de relatório |
| `503 Service Unavailable` | Não existe resumo diário consolidado no Redis para alguma data do período |
| `500 Internal Server Error` | Erro inesperado durante o processamento |

Falhas de comunicação com a API ou com o Redis também podem impedir a consolidação ou a geração do relatório. Erros inesperados são registrados nos logs, e a resposta genérica não expõe detalhes internos da exceção.

## 11. Configuração

As principais propriedades estão em `src/main/resources/application.yml`.

| Propriedade / variável | Finalidade | Valor padrão no projeto |
|---|---|---|
| `quarkus.http.port` | Porta HTTP da aplicação | `8081` |
| `FEEDBACK_API_URL` | URL-base da `feedback-api` | `http://localhost:8080` |
| `REDIS_HOST` | Endereço de conexão do Redis | `redis://localhost:6379` |
| `REDIS_PASSWORD` | Senha de acesso ao Redis | Valor simbólico local |
| `INTERNAL_SERVICE_TOKEN` | Segredo utilizado na autenticação entre serviços | Valor simbólico local |
| `app.security.maximo-dias-relatorio` | Quantidade máxima de dias permitida no relatório | `30` |

Os valores padrão de senha e token são destinados apenas à execução local. Em produção, utilize valores próprios e armazene segredos em mecanismos apropriados de configuração e gestão de segredos.

## 12. Execução local

### Pré-requisitos

- Java 25;
- Maven compatível com o projeto;
- Redis disponível;
- `feedback-api` em execução e acessível;
- token interno igual ao configurado na `feedback-api`.

### Executar em modo de desenvolvimento

Na raiz do módulo:

```bash
mvn quarkus:dev
```

A aplicação ficará disponível na porta `8081`, conforme a configuração atual.

Antes de executar, confira os valores de conexão com o Redis, URL da `feedback-api` e token interno no `application.yml` ou nas variáveis de ambiente do processo.

### Gerar o pacote

```bash
mvn clean package -DskipTests
```

O projeto utiliza o empacotamento Quarkus fast-jar. Após o build, a aplicação pode ser iniciada pelo artefato gerado em `target/quarkus-app/`:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## 13. Execução com Docker

O `Dockerfile` utiliza build em múltiplas etapas:

1. A imagem `maven:3.9.11-eclipse-temurin-25` compila e empacota a aplicação.
2. A imagem `eclipse-temurin:25-jre` executa o pacote Quarkus gerado.

Para construir a imagem, execute na raiz do módulo:

```bash
docker build -t function-report .
```

Exemplo de execução isolada do container:

```bash
docker run --rm \
  -p 8081:8081 \
  -e FEEDBACK_API_URL=http://host.docker.internal:8080 \
  -e REDIS_HOST=redis://host.docker.internal:6379 \
  -e REDIS_PASSWORD=<senha-do-redis> \
  -e INTERNAL_SERVICE_TOKEN=<token-interno> \
  function-report
```

O exemplo considera que a API e o Redis estão acessíveis pelo host Docker. Em uma execução por Docker Compose, utilize os nomes dos serviços na rede interna, conforme a configuração do projeto principal.

## 14. Considerações arquiteturais

A `function-report` mantém a responsabilidade de consolidação e consulta de relatórios separada da API principal:

- a `feedback-api` permanece responsável pelos dados transacionais e pelos cálculos agregados na origem;
- a `function-report` coordena a consolidação dos dados recebidos;
- o Redis funciona como armazenamento de leitura dos resumos diários;
- o relatório é montado a partir dos agregados, sem consultar individualmente os feedbacks nem acessar diretamente o banco relacional.

Essa separação reduz a dependência da geração de relatórios em relação às consultas transacionais e permite que a API principal e a função de relatórios evoluam de forma independente, mantendo entre elas um contrato HTTP interno.

Na arquitetura atual, o serviço Quarkus contém tanto os endpoints HTTP quanto o scheduler. Uma evolução para execução serverless gerenciada pode separar o acionamento agendado da API de consulta, mantendo o fluxo de consolidação e o contrato de dados.
