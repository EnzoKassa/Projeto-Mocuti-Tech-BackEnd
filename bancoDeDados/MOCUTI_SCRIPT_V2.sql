-- MySQL Script with schema updated to `mocuti`

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `mocuti` DEFAULT CHARACTER SET utf8 ;
USE `mocuti` ;

-- Table definitions remain the same, only REFERENCES updated to `mocuti`

CREATE TABLE IF NOT EXISTS `mocuti`.`endereco` (
  `id_endereco` INT NOT NULL auto_increment,
  `CEP` CHAR(8) NULL,
  `logradouro` VARCHAR(125) NULL,
  `numero` INT NULL,
  `complemento` VARCHAR(255) NULL,
  `uf` CHAR(2) NULL,
  `estado` VARCHAR(45) NULL,
  `bairro` VARCHAR(125) NULL,
  PRIMARY KEY (`id_endereco`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`canal_comunicacao` (
  `id_canal_comunicacao` INT NOT NULL auto_increment,
  `tipo_canal_comunicacao` VARCHAR(45) NULL,
  PRIMARY KEY (`id_canal_comunicacao`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`cargo` (
  `id_cargo` INT NOT NULL auto_increment,
  `tipo_cargo` VARCHAR(45) NULL,
  PRIMARY KEY (`id_cargo`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`usuario` (
  `id_usuario` INT NOT NULL auto_increment,
  `nome_completo` VARCHAR(45) NULL,
  `CPF` CHAR(14) NULL,
  `telefone` CHAR(11) NULL,
  `email` VARCHAR(45) NULL,
  `dt_nasc` DATE NULL,
  `genero` VARCHAR(45) NULL,
  `senha` VARCHAR(14) NULL,
  `is_ativo` TINYINT NULL,
  `dt_desativacao` DATE NULL,
  `is_autenticado` TINYINT NULL,
  `fk_endereco_usuario` INT NOT NULL,
  `fk_cargo_usuario` INT NOT NULL,
  `fk_canal_comunicacao_usuario` INT NOT NULL,
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `fk_usuario_endereco1`
    FOREIGN KEY (`fk_endereco_usuario`)
    REFERENCES `mocuti`.`endereco` (`id_endereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_canal_comunicacao1`
    FOREIGN KEY (`fk_canal_comunicacao_usuario`)
    REFERENCES `mocuti`.`canal_comunicacao` (`id_canal_comunicacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_cargo1`
    FOREIGN KEY (`fk_cargo_usuario`)
    REFERENCES `mocuti`.`cargo` (`id_cargo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`status_evento` (
  `id_status_evento` INT NOT NULL auto_increment,
  `situacao` VARCHAR(45) NULL,
  PRIMARY KEY (`id_status_evento`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`publico_alvo` (
  `id_publico_alvo` INT NOT NULL auto_increment,
  `tipo_publico` VARCHAR(45) NULL,
  PRIMARY KEY (`id_publico_alvo`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`categoria` (
  `id_categoria` INT NOT NULL auto_increment,
  `nome` VARCHAR(125) NULL,
  `descricao` VARCHAR(255) NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`evento` (
  `id_evento` INT NOT NULL auto_increment,
  `nome_evento` VARCHAR(45) NULL,
  `descricao` VARCHAR(255) NULL,
  `dia` DATE NULL,
  `hora_inicio` TIME NULL,
  `hora_fim` TIME NULL,
  `is_aberto` TINYINT NULL,
  `qtd_vaga` INT NULL,
  `qtd_interessado` INT NULL,
  `foto` MEDIUMBLOB NULL,
  `fk_endereco_evento` INT NOT NULL,
  `fk_status_evento` INT NOT NULL,
  `fk_publico_alvo_evento` INT NOT NULL,
  `fk_categoria_evento` INT NOT NULL,
  PRIMARY KEY (`id_evento`),
  CONSTRAINT `fk_evento_endereco1`
    FOREIGN KEY (`fk_endereco_evento`)
    REFERENCES `mocuti`.`endereco` (`id_endereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evento_status_evento1`
    FOREIGN KEY (`fk_status_evento`)
    REFERENCES `mocuti`.`status_evento` (`id_status_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evento_publico_alvo1`
    FOREIGN KEY (`fk_publico_alvo_evento`)
    REFERENCES `mocuti`.`publico_alvo` (`id_publico_alvo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evento_categoria1`
    FOREIGN KEY (`fk_categoria_evento`)
    REFERENCES `mocuti`.`categoria` (`id_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`status_inscricao` (
  `id_inscricao` INT NOT NULL auto_increment,
  `tipo_inscricao` VARCHAR(45) NULL,
  PRIMARY KEY (`id_inscricao`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`participacao` (
  `fk_usuario_participacao` INT NOT NULL,
  `fk_evento_participacao` INT NOT NULL,
  `is_inscrito` TINYINT NULL,
  `is_presente` TINYINT NULL,
  `fk_inscricao_participacao` INT NOT NULL,
  PRIMARY KEY (`fk_usuario_participacao`, `fk_evento_participacao`),
  CONSTRAINT `fk_usuario_has_evento_usuario`
    FOREIGN KEY (`fk_usuario_participacao`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_evento_evento1`
    FOREIGN KEY (`fk_evento_participacao`)
    REFERENCES `mocuti`.`evento` (`id_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_participacao_status_inscricao1`
    FOREIGN KEY (`fk_inscricao_participacao`)
    REFERENCES `mocuti`.`status_inscricao` (`id_inscricao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`nota_feedback` (
  `idnota` INT NOT NULL auto_increment,
  `tipo_nota` VARCHAR(45) NULL,
  PRIMARY KEY (`idnota`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`feedback` (
  `id_feedbak` INT NOT NULL auto_increment,
  `comentario` VARCHAR(255) NULL,
  `fk_evento_feedback` INT NOT NULL,
  `fk_usuario_feedback` INT NOT NULL,
  `fk_nota_feedback` INT NOT NULL,
  PRIMARY KEY (`id_feedbak`, `fk_nota_feedback`),
  CONSTRAINT `fk_feedbak_evento1`
    FOREIGN KEY (`fk_evento_feedback`)
    REFERENCES `mocuti`.`evento` (`id_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_feedback_usuario1`
    FOREIGN KEY (`fk_usuario_feedback`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_feedback_nota1`
    FOREIGN KEY (`fk_nota_feedback`)
    REFERENCES `mocuti`.`nota_feedback` (`idnota`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mocuti`.`preferencia` (
  `id_preferencia` INT NOT NULL auto_increment,
  `fk_usuario_preferencia` INT NOT NULL,
  `fk_categoria_preferencia` INT NOT NULL,
  PRIMARY KEY (`id_preferencia`, `fk_usuario_preferencia`, `fk_categoria_preferencia`),
  CONSTRAINT `fk_usuario_has_categoria_usuario1`
    FOREIGN KEY (`fk_usuario_preferencia`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_categoria_categoria1`
    FOREIGN KEY (`fk_categoria_preferencia`)
    REFERENCES `mocuti`.`categoria` (`id_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `endereco` (`id_endereco`, `CEP`, `logradouro`, `numero`, `complemento`, `uf`, `estado`, `bairro`) VALUES
(1, '01001000', 'Rua A', 100, 'Ap 1', 'SP', 'São Paulo', 'Centro'),
(2, '20040002', 'Av. B', 200, NULL, 'RJ', 'Rio de Janeiro', 'Copacabana');

INSERT INTO `canal_comunicacao` (`id_canal_comunicacao`, `tipo_canal_comunicacao`) VALUES
(1, 'E-mail'),
(2, 'SMS'),
(3, 'WhatsApp');

INSERT INTO `cargo` (`id_cargo`, `tipo_cargo`) VALUES
(1, 'Administrador'),
(2, 'Colaborador'),
(3, 'Participante');

INSERT INTO `usuario` (`id_usuario`, `nome_completo`, `CPF`, `telefone`, `email`, `dt_nasc`, `genero`, `senha`, `is_ativo`, `dt_desativacao`, `is_autenticado`, `fk_endereco_usuario`, `fk_cargo_usuario`, `fk_canal_comunicacao_usuario`) VALUES
(1, 'Maria Silva', '12345678901', '11999999999', 'maria@email.com', '1990-05-10', 'Feminino', 'senha123', 1, NULL, 1, 1, 1, 1),
(2, 'João Souza', '23456789012', '21988888888', 'joao@email.com', '1985-08-15', 'Masculino', 'senha456', 1, NULL, 0, 2, 2, 2);

INSERT INTO `status_evento` (`id_status_evento`, `situacao`) VALUES
(1, 'Aberto'),
(2, 'Fechado');

INSERT INTO `publico_alvo` (`id_publico_alvo`, `tipo_publico`) VALUES
(1, 'Estudantes'),
(2, 'Profissionais');

INSERT INTO `categoria` (`id_categoria`, `nome`, `descricao`) VALUES
(1, 'Tecnologia', 'Eventos sobre tecnologia'),
(2, 'Educação', 'Eventos educacionais');

INSERT INTO `evento` (`id_evento`, `nome_evento`, `descricao`, `dia`, `hora_inicio`, `hora_fim`, `is_aberto`, `qtd_vaga`, `qtd_interessado`, `foto`, `fk_endereco_evento`, `fk_status_evento`, `fk_publico_alvo_evento`, `fk_categoria_evento`) VALUES
(1, 'Workshop Python', 'Curso introdutório de Python', '2025-06-01', '09:00:00', '17:00:00', 1, 50, 30, NULL, 1, 1, 1, 1),
(2, 'Palestra Educação', 'Discussão sobre métodos de ensino', '2025-06-05', '14:00:00', '16:00:00', 1, 100, 60, NULL, 2, 1, 2, 2);

INSERT INTO `status_inscricao` (`id_inscricao`, `tipo_inscricao`) VALUES
(1, 'Confirmada'),
(2, 'Cancelada');

INSERT INTO `participacao` (`fk_usuario_participacao`, `fk_evento_participacao`, `is_inscrito`, `is_presente`, `fk_inscricao_participacao`) VALUES
(1, 1, 1, 1, 1),
(2, 2, 1, 0, 1);

INSERT INTO `nota_feedback` (`idnota`, `tipo_nota`) VALUES
(1, 'Ruim'),
(2, 'Boa'),
(3, 'Excelente');

INSERT INTO `feedback` (`id_feedbak`, `comentario`, `fk_evento_feedback`, `fk_usuario_feedback`, `fk_nota_feedback`) VALUES
(1, 'Evento muito bom!', 1, 1, 3),
(2, 'Precisa melhorar o som.', 2, 2, 1);

INSERT INTO `preferencia` (`id_preferencia`, `fk_usuario_preferencia`, `fk_categoria_preferencia`) VALUES
(1, 1, 1),
(2, 2, 2);

select * from endereco;
