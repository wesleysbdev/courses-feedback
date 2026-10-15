-- TABELAS
CREATE TABLE public.usuario
(
    id        uuid                   NOT NULL,
    ativo     boolean                NOT NULL,
    criado_em timestamp(6) without time zone NOT NULL,
    email     character varying(255) NOT NULL,
    excluido  boolean                NOT NULL,
    nome      character varying(200) NOT NULL,
    senha     character varying(255) NOT NULL,
    tipo      character varying(31)  NOT NULL,

    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT chk_usuario_tipo CHECK (tipo IN ('ADMINISTRADOR', 'PROFESSOR', 'ALUNO'))
);

CREATE TABLE public.curso
(
    id           UUID          NOT NULL,
    nome         VARCHAR(150)  NOT NULL,
    descricao    VARCHAR(1024) NOT NULL,
    professor_id UUID          NOT NULL,
    ativo        BOOLEAN       NOT NULL,
    criado_em    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pf_curso PRIMARY KEY (id),
    CONSTRAINT fk_curso_professor FOREIGN KEY (professor_id) REFERENCES usuario (id) ON DELETE RESTRICT
);

CREATE TABLE matricula
(
    id             UUID        NOT NULL,
    aluno_id       UUID        NOT NULL,
    curso_id       UUID        NOT NULL,
    status         VARCHAR(20) NOT NULL,
    matriculado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_matricula PRIMARY KEY (id),
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES usuario (id) ON DELETE RESTRICT,
    CONSTRAINT fk_matricula_curso FOREIGN KEY (curso_id) REFERENCES curso (id) ON DELETE RESTRICT,
    CONSTRAINT uk_matricula_aluno_curso UNIQUE (aluno_id, curso_id),
    CONSTRAINT chk_matricula_status CHECK (status IN ('EM_ANDAMENTO', 'CONCLUIDA'))
);

-- ÍNDICES
CREATE INDEX idx_curso_professor_id ON curso (professor_id);

CREATE INDEX idx_curso_ativo ON curso (ativo);

CREATE INDEX idx_matricula_curso_id ON matricula (curso_id);

CREATE INDEX idx_matricula_status ON matricula (status);