-- INSERTS PARA TESTE DA API

INSERT INTO Usuario (id_usuario, nome_completo, cpf, telefone, email, data_nascimento, genero, senha, is_ativo, dt_desativacao, is_autenticado, cargo_int, fk_endereco_usuario, fk_canal_comunicacao_usuario)
VALUES
-- Mulheres
(DEFAULT, 'Beatriz Oliveira', '111.222.333-44', '+55 71 91234-5678', 'beatriz.oliveira@email.com', '1990-02-20', 'Feminino', 'senha015', TRUE, DEFAULT, FALSE, 1, 1, 1),
(DEFAULT, 'Camila Santos', '222.333.444-55', '+55 81 98765-4321', 'camila.santos@email.com', '1994-08-15', 'Feminino', 'senha016', TRUE, DEFAULT, FALSE, 2, 2, 2),
(DEFAULT, 'Juliana Costa', '333.444.555-66', '+55 91 99876-5432', 'juliana.costa@email.com', '1996-12-05', 'Feminino', 'senha017', TRUE, DEFAULT, FALSE, 3, 3, 3),

-- Homens
(DEFAULT, 'Lucas Almeida', '444.555.666-77', '+55 61 91234-6789', 'lucas.almeida@email.com', '1987-03-10', 'Masculino', 'senha018', TRUE, DEFAULT, FALSE, 1, 1, 1),
(DEFAULT, 'Gabriel Souza', '555.666.777-88', '+55 71 98765-1234', 'gabriel.souza@email.com', '1992-06-25', 'Masculino', 'senha019', TRUE, DEFAULT, FALSE, 2, 2, 2),
(DEFAULT, 'Rafael Lima', '666.777.888-99', '+55 81 99876-2345', 'rafael.lima@email.com', '1995-09-30', 'Masculino', 'senha020', TRUE, DEFAULT, FALSE, 3, 3, 3),
(DEFAULT, 'Thiago Rocha', '777.888.999-00', '+55 91 91234-3456', 'thiago.rocha@email.com', '1990-11-12', 'Masculino', 'senha021', TRUE, DEFAULT, FALSE, 1, 1, 1);

INSERT INTO categoria (nome, descricao)
VALUES
    ('Palestra', 'Palestras tematicas com power point'),
    ('Doação', 'Evento de doação de roupas e/ou brinquedos'),
    ('Carnaval', 'Comemoração carnaval'),
    ('Apadrinhamento', 'Doação de brinquedos e roupas para crianças');

-- INSERTs iniciais para evento
INSERT INTO evento (
    nome_evento, descricao, dia, hora_inicio, hora_fim, is_aberto, qtd_vaga, qtd_interessado,
    foto,fk_endereco_evento,fk_status_evento,fk_publico_alvo_evento,fk_categoria_evento
) VALUES
      ('Palestra de Tecnologia', 'Evento sobre inovações em TI', '2025-06-15', '10:00:00', '12:00:00', TRUE, 100, 75, NULL,0,0,0,0),
      ('Workshop de Carreira', 'Orientações profissionais e networking', '2025-07-10', '14:00:00', '17:00:00', TRUE, 80, 60, NULL,0,0,0,0);

INSERT INTO endereco (CEP, logradouro, numero, complemento, UF, estado, bairro) VALUES
                                                                                    ('12345678', 'Rua A', 123, 'Apto 101', 'SP', 'São Paulo', 'Centro'),
                                                                                    ('98765432', 'Rua B', 456, 'Apto 202', 'RJ', 'Rio de Janeiro', 'Copacabana');
