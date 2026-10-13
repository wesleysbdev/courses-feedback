-- Table: public.usuario
CREATE TABLE public.usuario
(
    id uuid NOT NULL,
    ativo boolean NOT NULL,
    criado_em timestamp(6) without time zone NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    excluido boolean NOT NULL,
    nome character varying(200) COLLATE pg_catalog."default" NOT NULL,
    senha character varying(255) COLLATE pg_catalog."default" NOT NULL,
    tipo character varying(31) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT usuario_pkey PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT usuario_tipo_check CHECK (tipo::text = ANY (ARRAY['ADMINISTRADOR'::character varying, 'PROFESSOR'::character varying, 'ALUNO'::character varying]::text[]))
    );
