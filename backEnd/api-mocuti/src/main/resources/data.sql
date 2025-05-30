-- INSERTS PARA TESTE DA API

-- endereco
INSERT INTO endereco (CEP, logradouro, numero, complemento, UF, estado, bairro) VALUES
('12345678', 'Rua A', 123, 'Apto 101', 'SP', 'São Paulo', 'Centro'),
('98765432', 'Rua B', 456, 'Apto 202', 'RJ', 'Rio de Janeiro', 'Copacabana');

-- canal_comunicacao
INSERT INTO canal_comunicacao (tipo_canal_comunicacao) VALUES
('Email'),
('WhatsApp'),
('SMS');

-- cargo
INSERT INTO cargo (tipo_cargo) VALUES
('Administrador'),
('Usuário'),
('Moderador');

-- usuario
INSERT INTO usuario (nome_completo, cpf, telefone, email, data_nascimento, genero, senha, is_ativo,
dt_desativacao, is_autenticado, cargo_usuario, endereco_usuario, comunicacao_usuario)
VALUES
-- Mulheres
('Beatriz Oliveira', '111.222.333-44', '+55 71 91234-5678', 'beatriz.oliveira@email.com', '1990-02-20', 'Feminino', 'senha015', TRUE, DEFAULT, FALSE, 1, 1, 1),
('Camila Santos', '222.333.444-55', '+55 81 98765-4321', 'camila.santos@email.com', '1994-08-15', 'Feminino', 'senha016', TRUE, DEFAULT, FALSE, 2, 2, 2),
('Juliana Costa', '333.444.555-66', '+55 91 99876-5432', 'juliana.costa@email.com', '1996-12-05', 'Feminino', 'senha017', TRUE, DEFAULT, FALSE, 3, 2, 3),

-- Homens
('Lucas Almeida', '444.555.666-77', '+55 61 91234-6789', 'lucas.almeida@email.com', '1987-03-10', 'Masculino', 'senha018', TRUE, DEFAULT, FALSE, 1, 1, 1),
('Gabriel Souza', '555.666.777-88', '+55 71 98765-1234', 'gabriel.souza@email.com', '1992-06-25', 'Masculino', 'senha019', TRUE, DEFAULT, FALSE, 2, 2, 2),
('Rafael Lima', '666.777.888-99', '+55 81 99876-2345', 'rafael.lima@email.com', '1995-09-30', 'Masculino', 'senha020', TRUE, DEFAULT, FALSE, 3, 1, 3),
('Thiago Rocha', '777.888.999-00', '+55 91 91234-3456', 'thiago.rocha@email.com', '1990-11-12', 'Masculino', 'senha021', TRUE, DEFAULT, FALSE, 1, 1, 1);


-- status_evento
INSERT INTO status_evento (situacao) VALUES
('Aberto'),
('Fechado'),
('Cancelado');

-- publico_alvo
INSERT INTO publico_alvo (tipo_publico) VALUES
('Estudantes'),
('Profissionais'),
('Comunidade');

-- categoria
INSERT INTO categoria (nome, descricao) VALUES
('Palestra', 'Palestras tematicas com power point'),
('Doação', 'Evento de doação de roupas e/ou brinquedos'),
('Carnaval', 'Comemoração carnaval'),
('Apadrinhamento', 'Doação de brinquedos e roupas para crianças');

-- evento
INSERT INTO evento (
nome_evento, descricao, dia, hora_inicio, hora_fim, is_aberto, qtd_vaga, qtd_interessado,
foto, endereco_evento, status_evento, publico_evento, categoria_evento) VALUES
('Palestra de Tecnologia', 'Evento sobre inovações em TI', '2025-06-15','2025-07-10T14:00:00', '2025-07-10T14:00:00', TRUE, 100, 75, NULL, 1, 1, 1, 1),
('Workshop de Carreira', 'Orientações profissionais e networking', '2025-07-10', '2025-07-10T14:00:00', '2025-07-10T14:00:00', TRUE, 80, 60, NULL, 2, 2, 2, 2);

-- status_inscricao
INSERT INTO status_inscricao (tipo_inscricao) VALUES
('Pendente'),
('Confirmada'),
('Cancelada');

-- participacao
INSERT INTO participacao (is_inscrito, is_presente, inscricao_participacao, usuario_participacao, evento_participacao) VALUES
(1, 1, 1, 1, 1),
(2, 1, 2, 2, 2);

-- nota
INSERT INTO nota_feedback (tipo_nota) VALUES
('like'),
('dislike');

-- feedback
INSERT INTO feedback (comentario, feedback_evento, feedback_usuario, feedback_nota) VALUES
('Evento excelente, organização impecável!', 1, 1, 1),
('Poderia ter sido melhor. Faltou água.', 1, 2, 2),
('Gostei muito da palestra e dos palestrantes.', 2, 3, 1),
('Foi tudo ótimo, voltaria com certeza.', 2, 4, 1),
('Achei desorganizado e atrasado.', 1, 5, 2);

-- preferencia
INSERT INTO preferencia (preferencia_usuario, preferencia_categoria) VALUES
(1, 1),
(2, 2);
