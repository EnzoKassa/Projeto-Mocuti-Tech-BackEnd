-- As instruções DML (insert, update e delete)
-- aqui serão executadas quando a aplicação iniciar
-- PODEM haver quantas forem necessárias.
-- apenas separe elas com ponto e vírgula.

-- MAAAAS, para que funcione, é necessária a seguinte configuração no application.properties:
-- spring.jpa.defer-datasource-initialization=true


-- Tabela: usuario
CREATE TABLE usuario (
                         id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                         nome_completo VARCHAR(45),
                         CPF CHAR(14),
                         telefone CHAR(11),
                         email VARCHAR(45),
                         dt_nasc DATE,
                         genero VARCHAR(45),
                         senha VARCHAR(14),
                         is_ativo BOOLEAN,
                         dt_desativacao DATE,
                         is_autenticado BOOLEAN,
                         fk_endereco_usuario INT,
                         FOREIGN KEY (fk_endereco_usuario) REFERENCES endereco(id_endereco)
);

-- Tabela: evento
CREATE TABLE evento (
                        id_evento INT AUTO_INCREMENT PRIMARY KEY,
                        nome_evento VARCHAR(45),
                        descricao VARCHAR(255),
                        dia DATE,
                        hora_inicio TIME,
                        hora_fim TIME,
                        is_aberto BOOLEAN,
                        qtd_vaga INT,
                        qtd_interessado INT,
                        foto BLOB,
                        fk_endereco_evento INT,
                        FOREIGN KEY (fk_endereco_evento) REFERENCES endereco(id_endereco)
);

-- INSERTs iniciais para usuario
INSERT INTO usuario (
    nome_completo, CPF, telefone, email, dt_nasc, genero, senha, is_ativo,
    dt_desativacao, is_autenticado
) VALUES
      ('Maria Silva', '12345678900011', '11999999999', 'maria@email.com', '1990-05-10', 'Feminino', 'senha123', TRUE, NULL, TRUE),
      ('João Souza', '98765432100022', '11888888888', 'joao@email.com', '1988-07-20', 'Masculino', 'senha456', TRUE, NULL, TRUE);

-- INSERTs iniciais para evento
INSERT INTO evento (
    nome_evento, descricao, dia, hora_inicio, hora_fim, is_aberto, qtd_vaga, qtd_interessado,
    foto
) VALUES
      ('Palestra de Tecnologia', 'Evento sobre inovações em TI', '2025-06-15', '10:00:00', '12:00:00', TRUE, 100, 75, NULL),
      ('Workshop de Carreira', 'Orientações profissionais e networking', '2025-07-10', '14:00:00', '17:00:00', TRUE, 80, 60, NULL);

INSERT INTO endereco (CEP, logradouro, numero, complemento, UF, estado, bairro) VALUES
                                                                                    ('12345678', 'Rua A', 123, 'Apto 101', 'SP', 'São Paulo', 'Centro'),
                                                                                    ('98765432', 'Rua B', 456, 'Apto 202', 'RJ', 'Rio de Janeiro', 'Copacabana');

