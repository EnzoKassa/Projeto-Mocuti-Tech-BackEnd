CREATE DATABASE mocuti;
USE mocuti;

-- Tabela de usuarios do sistema
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(15),
    data_nascimento DATE,
    cep VARCHAR(10) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(100),
    cargo ENUM('voluntario', 'administracao', 'beneficiario') DEFAULT 'beneficiario',
    area_atuacao_voluntario VARCHAR(50),
    necessidade_beneficiario TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de eventos
CREATE TABLE evento (
    id_evento INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    data_evento DATETIME NOT NULL,
    recorrencia BOOLEAN,
    quantidade_de_participantes INT,
    local VARCHAR(200),
    fk_responsavel INT, 
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_voluntario) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

-- Tabela de participação nos eventos
CREATE TABLE participacao (
    fk_voluntario INT NOT NULL, 
    fk_evento INT NOT NULL, 
    presenca ENUM('confirmado', 'compareceu', 'ausente') DEFAULT 'confirmado',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_voluntario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (fk_evento) REFERENCES evento(id_evento) ON DELETE CASCADE,
    CONSTRAINT pk_participacao PRIMARY KEY (fk_voluntario, fk_evento)
);
