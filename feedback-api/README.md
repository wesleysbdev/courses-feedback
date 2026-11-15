# Feedback API

API principal da plataforma **Courses Feedback**. É responsável pelo cadastro e gerenciamento dos usuários, cursos e matrículas, pelo recebimento e armazenamento dos feedbacks e pela integração com os serviços de notificação e geração de relatórios.

A aplicação também funciona como ponto de entrada para a consulta de relatórios: recebe a solicitação autenticada do administrador e a encaminha para a `function-report`.

## 1. Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 25 | Linguagem e runtime da aplicação |
| Spring Boot 4.1.0 | Inicialização e organização da aplicação |
| Spring Web MVC | Exposição dos endpoints REST |
| Spring Data JPA / Hibernate | Persistência e mapeamento objeto-relacional |
| Spring Security | Autenticação, autorização e proteção dos endpoints |
| OAuth2 Resource Server / JWT | Emissão e validação dos tokens JWT |
| Bean Validation | Validação dos dados recebidos nas requisições |
| Flyway | Versionamento e execução das migrações do banco |
| PostgreSQL | Banco de dados relacional |
| Spring AMQP | Publicação de eventos no RabbitMQ |
| RabbitMQ | Mensageria para notificação de feedbacks críticos |
| MapStruct | Conversão entre entidades, modelos de persistência e DTOs |
| Lombok | Redução de código repetitivo |
| Maven | Gerenciamento de dependências e build |
| Docker | Empacotamento da aplicação em contêiner |

## 2. Organização do código

O código é organizado por módulos funcionais. Dentro de cada módulo, as responsabilidades são separadas entre domínio, serviços, interface HTTP e persistência.

```text
src/main/java/br/com/fiap/feedback/
├── autenticacao/
│   ├── domain/
│   ├── service/
│   └── web/
├── curso/
│   ├── domain/
│   ├── persistence/
│   ├── service/
│   └── web/
├── matricula/
│   ├── domain/
│   ├── persistence/
│   ├── service/
│   └── web/
├── feedback/
│   ├── domain/
│   ├── integracao/
│   ├── persistence/
│   ├── service/
│   └── web/
├── usuario/
│   ├── domain/
│   ├── persistence/
│   ├── service/
│   └── web/
├── infraestructure/
│   ├── annotation/
│   ├── exception/
│   ├── mapper/
│   ├── mensageria/
│   ├── password/
│   └── security/
├── shared/
└── Main.java
```

- **`domain`**: entidades, objetos de valor, enums e exceções que representam conceitos e regras do negócio.
- **`service`**: coordena os fluxos da aplicação e aplica as regras de negócio.
- **`web`**: controllers REST, DTOs de entrada e saída e mapeadores.
- **`persistence`**: modelos JPA, repositórios e projeções usadas nas consultas.
- **`integracao`**: configuração e cliente HTTP para comunicação com a `function-report`.
- **`infraestructure`**: configurações transversais de segurança, mensageria, codificação de senha e tratamento de exceções.
- **`shared`**: componentes compartilhados, como validação de permissões e geração de identificadores.

## 3. Pré-requisitos

Para executar a API localmente, são necessários:

- JDK 25;
- Maven compatível com o projeto;
- PostgreSQL;
- RabbitMQ.

A `function-report` não é necessária para iniciar a API, mas precisa estar disponível para que o endpoint público de geração de relatórios funcione.

Na execução integrada do projeto, os serviços são orquestrados pelo `docker-compose.yml` localizado na raiz do repositório. O Dockerfile deste módulo utiliza uma imagem Maven com Temurin 25 para compilar e uma imagem Temurin 25 JRE para executar a aplicação.

## 4. Configuração

As configurações ficam em `src/main/resources/application.yml`. Os valores podem ser sobrescritos por variáveis de ambiente.

| Variável | Finalidade | Valor padrão local |
|---|---|---|
| `DB_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/courses-feedback?options=-c%20timezone=America/Sao_Paulo` |
| `DB_USERNAME` | Usuário do banco | `postgres` |
| `DB_PASSWORD` | Senha do banco | `postgres` |
| `JPA_DDL_AUTO` | Estratégia de validação do schema pelo Hibernate | `validate` |
| `JWT_ISSUER` | Emissor dos tokens JWT | `feedback-api` |
| `JWT_SECRET` | Segredo usado para assinar e validar JWT | Definido como fallback local no YAML |
| `JWT_EXPIRATION` | Duração do token, em segundos | `3600` |
| `RABBITMQ_HOST` | Host do RabbitMQ | `localhost` |
| `RABBITMQ_PORT` | Porta AMQP do RabbitMQ | `5672` |
| `RABBITMQ_USERNAME` | Usuário do RabbitMQ | `feedback` |
| `RABBITMQ_PASSWORD` | Senha do RabbitMQ | `feedback` |
| `FUNCTION_REPORT_URL` | URL base da `function-report` | `http://localhost:8081` |
| `INTERNAL_SERVICE_TOKEN` | Token compartilhado para comunicação interna | Definido como fallback local no YAML |

A aplicação utiliza o prefixo `/api/v1` para os endpoints HTTP e o fuso horário `America/Sao_Paulo` para o processamento de datas. O banco também recebe essa configuração de timezone pela migração inicial.

## 5. Banco de dados

O schema é criado e versionado pelo Flyway. As migrações estão em:

```text
src/main/resources/db/migration/
```

O Hibernate está configurado com `ddl-auto: validate`: ele valida o mapeamento com o schema existente, mas não cria nem altera as tabelas. A responsabilidade de evolução do schema fica com as migrações Flyway.

### 5.1 Tabelas

| Tabela | Responsabilidade | Campos principais |
|---|---|---|
| `usuario` | Armazena alunos, professores e administradores | `id`, `nome`, `email`, `senha`, `tipo`, `ativo`, `excluido`, `criado_em` |
| `curso` | Armazena os cursos disponíveis | `id`, `nome`, `descricao`, `professor_id`, `ativo`, `criado_em` |
| `matricula` | Relaciona um aluno a um curso | `id`, `aluno_id`, `curso_id`, `status`, `matriculado_em` |
| `feedback` | Armazena a avaliação enviada para uma matrícula | `id`, `matricula_id`, `descricao`, `nota`, `criado_em` |

### 5.2 Relacionamentos e restrições

- Um **curso** referencia um **professor**, por meio de `curso.professor_id` → `usuario.id`.
- Uma **matrícula** referencia um **aluno** e um **curso**, por meio de `matricula.aluno_id` → `usuario.id` e `matricula.curso_id` → `curso.id`.
- Uma **matrícula pode possuir no máximo um feedback**. A coluna `feedback.matricula_id` possui chave estrangeira e restrição `UNIQUE`.
- A combinação `(aluno_id, curso_id)` é única em `matricula`, impedindo mais de uma matrícula para o mesmo aluno e curso.
- O e-mail de `usuario` é único.
- A coluna `feedback.nota` possui uma restrição no banco para aceitar somente valores entre 0 e 10.
- O campo `usuario.tipo` aceita `ADMINISTRADOR`, `PROFESSOR` e `ALUNO`.
- O campo `matricula.status` aceita `EM_ANDAMENTO` e `CONCLUIDA`.

As relações de usuário e curso utilizadas por matrícula/curso têm restrição de exclusão (`ON DELETE RESTRICT`). A chave estrangeira de feedback para matrícula está configurada com `ON DELETE CASCADE`.

### 5.3 Índices

A migração inicial cria índices para `curso.professor_id`, `curso.ativo`, `matricula.curso_id`, `matricula.status`, `feedback.matricula_id` e `feedback.criado_em`, além das chaves primárias e restrições únicas descritas acima.

### 5.4 Dados iniciais

A migração inicial do schema é `V20261010180900__criacao_schema_inicial.sql`. A segunda migração (`V20261010180910__carga_inicial.sql`) insere dados de demonstração: usuários, professores, cursos, matrículas e feedbacks com notas e datas geradas aleatoriamente entre `2026-11-01` e `2026-11-17`. O comentário da migração informa a senha inicial de demonstração (`Admin@123`); o administrador inicial utiliza o e-mail `admin@email.com`.

Esses registros servem para execução e demonstração local. Antes de utilizar as migrações em um ambiente de produção, revise a carga de dados de demonstração e substitua ou remova as credenciais e os registros fixos.

## 6. API REST

A URL-base local é:

```text
http://localhost:8080/api/v1
```

Os endpoints, salvo indicação contrária, exigem um token JWT no cabeçalho:

```http
Authorization: Bearer <token>
```

### 6.1 Autenticação

| Método | Endpoint | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/autenticacao` | Público | Valida e-mail e senha e retorna um JWT |

Exemplo de requisição:

```json
{
  "email": "admin@email.com",
  "senha": "Admin@123"
}
```

Resposta:

```json
{
  "token": "<jwt>"
}
```

O token contém o identificador do usuário no `sub`, o perfil no claim `role`, o nome no claim `nome`, o emissor e os instantes de emissão e expiração.

### 6.2 Usuários

| Método | Endpoint | Acesso / regra |
|---|---|---|
| `POST` | `/usuarios` | Somente `ADMINISTRADOR` cadastra usuários |
| `GET` | `/usuarios` | Somente `ADMINISTRADOR` lista usuários |
| `GET` | `/usuarios/{id}` | O próprio usuário ou `ADMINISTRADOR` |
| `PUT` | `/usuarios/{id}` | O próprio usuário ou `ADMINISTRADOR` pode editar nome e e-mail |
| `PATCH` | `/usuarios/{id}/senha` | O próprio usuário ou `ADMINISTRADOR` pode alterar a senha |
| `PATCH` | `/usuarios/{id}/ativar` | Somente `ADMINISTRADOR` |
| `PATCH` | `/usuarios/{id}/inativar` | Somente `ADMINISTRADOR` |
| `DELETE` | `/usuarios/{id}` | Somente `ADMINISTRADOR`; realiza exclusão lógica |

A senha é codificada com BCrypt antes de ser persistida. A exclusão lógica marca o usuário como excluído e inativo, sem remover o registro do banco.

### 6.3 Cursos

| Método | Endpoint | Acesso / regra |
|---|---|---|
| `POST` | `/cursos` | Somente `ADMINISTRADOR`; o professor informado deve existir, ser do tipo `PROFESSOR` e estar ativo |
| `GET` | `/cursos` | Qualquer usuário autenticado; retorna cursos ativos |
| `GET` | `/cursos/{id}` | Qualquer usuário autenticado |

### 6.4 Matrículas

| Método | Endpoint | Acesso / regra |
|---|---|---|
| `POST` | `/matriculas` | O aluno autenticado é usado como titular da matrícula; o curso precisa estar ativo e não pode existir outra matrícula para o mesmo aluno e curso |
| `GET` | `/matriculas` | Qualquer usuário autenticado |
| `GET` | `/matriculas/{id}` | Qualquer usuário autenticado |

O corpo do `POST /matriculas` contém o identificador do curso:

```json
{
  "cursoId": "<uuid-do-curso>"
}
```

### 6.5 Feedbacks

| Método | Endpoint | Acesso / regra |
|---|---|---|
| `POST` | `/feedbacks` | Somente `ALUNO`; a matrícula deve pertencer ao aluno autenticado |
| `GET` | `/feedbacks` | Qualquer usuário autenticado |
| `GET` | `/feedbacks/{id}` | Qualquer usuário autenticado |
| `GET` | `/feedbacks/relatorios/inicio/{inicio}/fim/{fim}` | Somente `ADMINISTRADOR`; datas no formato `YYYY-MM-DD` |

Corpo da requisição para criação:

```json
{
  "matriculaId": "<uuid-da-matricula>",
  "descricao": "Conteúdo claro e atividades práticas muito úteis.",
  "nota": 8
}
```

A nota deve ser um inteiro de 0 a 10 e a descrição não pode ser vazia. A regra de negócio verifica se a matrícula pertence ao aluno autenticado. O banco também impede mais de um feedback para a mesma matrícula.

A classificação calculada pelo objeto de valor `FeedbackAvaliacao` é:

| Nota | Classificação no domínio |
|---|---|
| 0 a 4 | `RUIM` |
| 5 a 7 | `MEDIO` |
| 8 a 10 | `BOM` |

### 6.6 Endpoint interno de agregação diária

| Método | Endpoint | Autorização |
|---|---|---|
| `GET` | `/internal/feedbacks/agregados-diarios/{data}` | Cabeçalho `X-Internal-Service-Token` com o token compartilhado entre a API e a `function-report` |

Exemplo de caminho completo:

```text
GET /api/v1/internal/feedbacks/agregados-diarios/2026-11-10
```

Resposta:

```json
{
  "data": "2026-11-10",
  "somaNotas": 24,
  "totalAvaliacoes": 4,
  "avaliacoesCriticas": 1,
  "avaliacoesNormais": 1,
  "avaliacoesBoas": 2
}
```

A consulta considera o início do dia informado, inclusive, até o início do dia seguinte, exclusivo. Os agregados são utilizados pela `function-report` para consolidar os dados no Redis.

## 7. Fluxos de integração

### 7.1 Notificação via RabbitMQ

Quando um aluno cadastra um feedback, o `FeedbackService` valida o perfil, localiza a matrícula, confere a titularidade, cria o objeto de domínio e persiste o feedback. Se a classificação for `RUIM` (nota de 0 a 4), a API publica uma mensagem para a `function-notification`.

A topologia declarada pela API é:

| Elemento | Nome |
|---|---|
| Exchange | `feedback.eventos` |
| Tipo | `topic` |
| Fila | `notificacao.feedback.ruim` |
| Routing key | `feedback.ruim` |

A mensagem é construída pelo DTO `FeedbackNotificacao`, que contém a versão do evento, o identificador do feedback, a descrição, a avaliação e os dados da matrícula. A publicação é feita pelo `RabbitTemplate` através do serviço `NotificacaoService`.

A aplicação declara exchange, fila e binding na inicialização usando `RabbitAdmin`. A fila e a exchange são duráveis.

> Na versão atual, a publicação é direta pelo `RabbitTemplate` durante o fluxo transacional do cadastro do feedback. Não há implementação de Outbox neste módulo.

### 7.2 Relatórios via function-report

O administrador consulta o endpoint:

```text
GET /api/v1/feedbacks/relatorios/inicio/{inicio}/fim/{fim}
```

A API valida o perfil do usuário e delega a consulta para `FunctionReportClient`, que utiliza `RestClient` para chamar a `function-report`. A chamada interna inclui o cabeçalho `X-Internal-Service-Token`.

A `function-report` consulta os agregados diários por meio do endpoint interno descrito na seção 6.6. A API retorna ao administrador o DTO `FeedbackRelatorioResponse`, com o período, média das notas, quantidade total, totais por categoria e quantidade de avaliações por dia.

Quando a `function-report` retorna um erro HTTP estruturado, a API converte a resposta para `FunctionReportIntegracaoException` e preserva o status HTTP e o conteúdo de erro recebidos, quando disponíveis.

## 8. Segurança

A segurança é implementada com Spring Security em modo stateless. A aplicação não utiliza sessão HTTP para manter a autenticação.

### 8.1 Autenticação JWT

1. O usuário envia e-mail e senha para `POST /api/v1/autenticacao`.
2. A aplicação localiza o usuário, verifica se está ativo e não excluído e compara a senha informada com o hash BCrypt armazenado.
3. Se as credenciais forem válidas, a aplicação emite um JWT assinado com HMAC SHA-256 (HS256).
4. Nas requisições protegidas, o Spring Security valida o token, incluindo emissor e validade temporal.
5. O `UserTokenValidator` consulta o usuário no PostgreSQL usando o `sub` do token. Tokens associados a usuários inexistentes, inativos ou excluídos são rejeitados.

O claim `role` é convertido em autoridade Spring com o prefixo `ROLE_`. Os perfis de negócio disponíveis são `ADMINISTRADOR`, `PROFESSOR` e `ALUNO`.

### 8.2 Autorização

A configuração geral permite sem JWT apenas `POST /autenticacao/**` e o caminho de erro `/error`. Os demais endpoints exigem autenticação. A autorização por perfil é complementada por `@PreAuthorize` e pelas validações realizadas nos serviços, por exemplo, ao cadastrar cursos, criar feedbacks ou gerar relatórios.

### 8.3 Comunicação interna

O endpoint de agregação diária não utiliza o JWT do usuário final. Ele é protegido por um filtro dedicado que exige o cabeçalho `X-Internal-Service-Token`. A comparação dos tokens usa `MessageDigest.isEqual`. Quando o token é válido, o filtro atribui a autoridade `ROLE_INTERNAL_SERVICE`, exigida pela configuração de segurança para o caminho `/api/v1/internal/**`.

O mesmo segredo compartilhado é utilizado pela API ao chamar a `function-report`. Esse mecanismo é destinado à comunicação serviço a serviço e deve ser protegido por segredo próprio, armazenado em mecanismo seguro de configuração no ambiente de implantação.

### 8.4 Tratamento de erros

As exceções da aplicação são tratadas centralmente por `GerenciamentoGlobalExceptionHandler`. Entre os casos tratados estão erros de validação, regras de negócio, incompatibilidade de parâmetros, recurso inexistente, acesso negado e erros propagados pela integração com a `function-report`.

## 9. Execução local

### 9.1 Com Maven

Primeiro, disponibilize o PostgreSQL e o RabbitMQ e configure as variáveis de ambiente conforme a seção 4. Em seguida, na pasta `feedback-api`:

```bash
mvn spring-boot:run
```

Para gerar o artefato:

```bash
mvn clean package -DskipTests
```

Para executar o JAR gerado:

```bash
java -Duser.timezone=America/Sao_Paulo -jar target/feedback-api-1.0.0.jar
```

A API fica disponível, por padrão, em `http://localhost:8080/api/v1`.

### 9.2 Com Docker

Na pasta `feedback-api`, o Dockerfile pode ser utilizado para gerar a imagem:

```bash
docker build -t feedback-api:local .
```

A execução do contêiner depende de variáveis de ambiente e de conectividade com PostgreSQL e RabbitMQ. Para a execução integrada do projeto, prefira utilizar o `docker-compose.yml` da raiz do repositório, que configura a rede entre os módulos e os serviços de infraestrutura.

## 10. Observações sobre o estado atual

- O módulo não inclui uma suíte de testes automatizados versionada no diretório `src/test` nesta versão do pacote.
- A validação funcional foi conduzida manualmente com requisições HTTP durante o desenvolvimento. A coleção de requisições utilizada na demonstração fica no repositório principal, não neste módulo compactado.
- A migração de dados iniciais contém registros de demonstração e feedbacks gerados aleatoriamente; revise esse conteúdo antes de reutilizar as migrações em produção.
- O `pom.xml` não inclui dependência de Swagger/OpenAPI nesta versão; a referência dos endpoints está descrita neste README.
- A classificação no domínio usa os nomes `RUIM`, `MEDIO` e `BOM`; os DTOs de agregação e relatório usam os nomes `avaliacoesCriticas`, `avaliacoesNormais` e `avaliacoesBoas` para os mesmos intervalos de notas.
