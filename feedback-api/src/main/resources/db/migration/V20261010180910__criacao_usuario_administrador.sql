-- USUÁRIOS
-- Senhas: Admin@123
INSERT INTO public.usuario(tipo, id, ativo, criado_em, email, excluido, nome, senha)
VALUES 
        ('ADMINISTRADOR', '01a127a9-9d2e-73be-803a-2be219feb875', true, now(), 'admin@email.com', false, 'Usuário Admin', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('PROFESSOR', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', true, now(), 'carlos.mendes@escola.com', false, 'Carlos Mendes', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('PROFESSOR', '01a13d82-0c53-752b-b6df-480f654add4e', true, now(), 'fernanda.oliveira@escola.com', false, 'Fernanda Oliveira', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a13d8b-74c4-748f-99ba-dfd18755431f', true, now(), 'jose@aluno.com', false, 'José Aluno', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a13d8b-8fbf-7169-9481-ba1f50827bd1', true, now(), 'elias@aluno.com', false, 'Elias Aluno', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y');
        ('ALUNO', '01a14100-1111-7111-8111-111111111101', true, now(), 'ana.souza@aluno.com', false, 'Ana Souza', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-2222-7222-8222-222222222202', true, now(), 'bruno.lima@aluno.com', false, 'Bruno Lima', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-3333-7333-8333-333333333303', true, now(), 'camila.rocha@aluno.com', false, 'Camila Rocha', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-4444-7444-8444-444444444404', true, now(), 'daniel.costa@aluno.com', false, 'Daniel Costa', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-5555-7555-8555-555555555505', true, now(), 'beatriz.silva@aluno.com', false, 'Beatriz Silva', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-6666-7666-8666-666666666606', true, now(), 'lucas.martins@aluno.com', false, 'Lucas Martins', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-7777-7777-8777-777777777707', true, now(), 'mariana.alves@aluno.com', false, 'Mariana Alves', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-8888-7888-8888-888888888808', true, now(), 'gabriel.ribeiro@aluno.com', false, 'Gabriel Ribeiro', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-9999-7999-8999-999999999909', true, now(), 'julia.pereira@aluno.com', false, 'Júlia Pereira', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y'),
        ('ALUNO', '01a14100-aaaa-7aaa-8aaa-aaaaaaaaaa10', true, now(), 'rafael.santos@aluno.com', false, 'Rafael Santos', '$2a$12$84dH.i8Qv6FSMYcuVLkVJeZAinR4W/9227.pCFFUBk657RewS7l2y');

-- CURSOS
INSERT INTO curso (id, nome, descricao, professor_id, ativo, criado_em)
VALUES 
        ('01a13d88-bab2-722a-be42-86d837120ee6', 'Java para Iniciantes', 'Fundamentos da linguagem Java, orientação a objetos e boas práticas de programação.', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', TRUE, now()),
        ('01a13d88-d4f2-703e-acd5-88256d3982c7', 'Spring Boot Avançado', 'Desenvolvimento de APIs REST, segurança, persistência com JPA e arquitetura de aplicações Spring.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now()),
        ('01a13d88-efc3-76de-adba-e93ce08251ae', 'Banco de Dados com PostgreSQL', 'Modelagem relacional, SQL, constraints, índices e otimização de consultas.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now()),
        ('01a14200-0001-7001-8001-000000000001', 'Arquitetura de Microsserviços com Spring Cloud', 'Construção de sistemas distribuídos, descoberta de serviços, gateway e resiliência.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now()),
        ('01a14200-0002-7002-8002-000000000002', 'Docker e Kubernetes na Prática', 'Containerização de aplicações, gerenciamento de clusters e implantação em nuvem.', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', TRUE, now()),
        ('01a14200-0003-7003-8003-000000000003', 'Desenvolvimento Frontend com Angular', 'Criação de SPAs modernas, componentes reativos, rotas e consumo de APIs REST.', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', TRUE, now()),
        ('01a14200-0004-7004-8004-000000000004', 'React.js Essencial', 'Construção de interfaces modernas baseadas em componentes utilizando React e TypeScript.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now()),
        ('01a14200-0005-7005-8005-000000000005', 'Testes Automatizados em Java (JUnit e Mockito)', 'Garantia de qualidade de software, testes unitários, de integração e mocks.', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', TRUE, now()),
        ('01a14200-0006-7006-8006-000000000006', 'Segurança em Aplicações Web (OAuth2 e JWT)', 'Implementação de autenticação, autorização e proteção contra vulnerabilidades comuns.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now()),
        ('01a14200-0007-7007-8007-000000000007', 'Python para Análise de Dados e IA', 'Fundamentos de Python, manipulação de dados com Pandas e introdução a Machine Learning.', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', TRUE, now()),
        ('01a14200-0008-7008-8008-000000000008', 'Desenvolvimento Ágil com Scrum e Kanban', 'Metodologias ágeis, gestão de projetos de software e fluxo de entrega contínua.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now()),
        ('01a14200-0009-7009-8009-000000000009', 'Clean Code e Arquitetura Limpa', 'Princípios SOLID, refatoração de código legado e design de software desacoplado.', '01a13d81-c7b9-76b8-9803-fc6c87796f7c', TRUE, now()),
        ('01a14200-0010-7010-8010-000000000010', 'DevOps e Integração Contínua (CI/CD)', 'Automação de pipelines de build, testes e deploy utilizando GitHub Actions.', '01a13d82-0c53-752b-b6df-480f654add4e', TRUE, now());

-- MATRICULAS

-- INSERT INTO matricula (id, aluno_id, curso_id, status, matriculado_em)
-- VALUES 
-- ('01a13d8d-9e3f-739f-9275-d4af524965e1', '01a13d8b-74c4-748f-99ba-dfd18755431f', '01a13d88-bab2-722a-be42-86d837120ee6', 'EM_ANDAMENTO', now()),
-- ('01a13d8e-248b-77bb-84e0-867799e9d5a9', '01a13d8b-74c4-748f-99ba-dfd18755431f', '01a13d88-d4f2-703e-acd5-88256d3982c7', 'EM_ANDAMENTO', now()),
-- ('01a13d8e-3ec9-7252-9729-7277588ad8c2', '01a13d8b-8fbf-7169-9481-ba1f50827bd1', '01a13d88-efc3-76de-adba-e93ce08251ae', 'EM_ANDAMENTO', now()),

INSERT INTO matricula (id, aluno_id, curso_id, status, matriculado_em)
SELECT 
    gen_random_uuid() AS id,
    u.id AS aluno_id,
    c.id AS curso_id,
    'EM_ANDAMENTO' AS status,
    now() AS matriculado_em
FROM public.usuario u
CROSS JOIN curso c
WHERE u.tipo = 'ALUNO';


-- FEEDBACKS

INSERT INTO public.feedback (id, matricula_id, descricao, nota, criado_em)
SELECT 
    gen_random_uuid() AS id,
    m.id AS matricula_id,
    'Curso muito proveitoso e com excelente conteúdo prático.' AS descricao,
    floor(random() * 11)::integer AS nota, -- Gera um número inteiro aleatório de 0 a 10
    to_timestamp(
        extract(epoch from timestamp '2026-11-01 00:00:00') + 
        random() * (
            extract(epoch from timestamp '2026-11-17 23:59:59') - 
            extract(epoch from timestamp '2026-11-01 00:00:00')
        )
    ) AS criado_em -- Data aleatória entre 01/11/2026 e 17/11/2026
FROM public.matricula m
WHERE NOT EXISTS (
    SELECT 1 FROM public.feedback f WHERE f.matricula_id = m.id
);