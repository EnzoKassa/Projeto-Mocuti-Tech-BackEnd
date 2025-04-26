-- Script ajustado para mocuti

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mocuti
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mocuti` DEFAULT CHARACTER SET utf8 ;
USE `mocuti` ;

-- -----------------------------------------------------
-- Table `mocuti`.`endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`endereco` (
  `id_endereco` INT NOT NULL,
  `CEP` CHAR(8) NULL,
  `logradouro` VARCHAR(125) NULL,
  `numero` INT NULL,
  `complemento` VARCHAR(255) NULL,
  `uf` CHAR(2) NULL,
  `estado` VARCHAR(45) NULL,
  `bairro` VARCHAR(125) NULL,
  PRIMARY KEY (`id_endereco`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`endereco` (id_endereco, CEP, logradouro, numero, complemento, uf, estado, bairro)
VALUES (1, '12345678', 'Rua Exemplo', 100, 'Apto 101', 'SP', 'São Paulo', 'Centro');

-- -----------------------------------------------------
-- Table `mocuti`.`canal_comunicacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`canal_comunicacao` (
  `id_canal_comunicacao` INT NOT NULL,
  `tipo_canal_comunicacao` VARCHAR(45) NULL,
  PRIMARY KEY (`id_canal_comunicacao`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`canal_comunicacao` (id_canal_comunicacao, tipo_canal_comunicacao)
VALUES (1, 'Email');

-- -----------------------------------------------------
-- Table `mocuti`.`cargo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`cargo` (
  `id_cargo` INT NOT NULL,
  `tipo_cargo` VARCHAR(45) NULL,
  PRIMARY KEY (`id_cargo`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`cargo` (id_cargo, tipo_cargo)
VALUES (1, 'Coordenador');

-- -----------------------------------------------------
-- Table `mocuti`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`usuario` (
  `id_usuario` INT NOT NULL,
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
  INDEX `fk_usuario_endereco1_idx` (`fk_endereco_usuario` ASC) VISIBLE,
  INDEX `fk_usuario_canal_comunicacao1_idx` (`fk_canal_comunicacao_usuario` ASC) VISIBLE,
  INDEX `fk_usuario_cargo1_idx` (`fk_cargo_usuario` ASC) VISIBLE,
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
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`usuario` (id_usuario, nome_completo, CPF, telefone, email, dt_nasc, genero, senha, is_ativo, dt_desativacao, is_autenticado, fk_endereco_usuario, fk_cargo_usuario, fk_canal_comunicacao_usuario)
VALUES (1, 'João Silva', '123.456.789-00', '11999999999', 'joao@email.com', '1990-01-01', 'Masculino', 'senha123', 1, NULL, 1, 1, 1, 1);

-- -----------------------------------------------------
-- Table `mocuti`.`status_evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`status_evento` (
  `id_status_evento` INT NOT NULL,
  `situacao` VARCHAR(45) NULL,
  PRIMARY KEY (`id_status_evento`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`status_evento` (id_status_evento, situacao)
VALUES (1, 'Confirmado');

-- -----------------------------------------------------
-- Table `mocuti`.`publico_alvo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`publico_alvo` (
  `id_publico_alvo` INT NOT NULL,
  `tipo_publico` VARCHAR(45) NULL,
  PRIMARY KEY (`id_publico_alvo`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`publico_alvo` (id_publico_alvo, tipo_publico)
VALUES (1, 'Adulto');

-- -----------------------------------------------------
-- Table `mocuti`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`categoria` (
  `id_categoria` INT NOT NULL,
  `nome` VARCHAR(125) NULL,
  `descricao` VARCHAR(255) NULL,
  PRIMARY KEY (`id_categoria`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`categoria` (id_categoria, nome, descricao)
VALUES (1, 'Oficina', 'Atividades práticas e educativas');

-- -----------------------------------------------------
-- Table `mocuti`.`evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`evento` (
  `id_evento` INT NOT NULL,
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
  INDEX `fk_evento_endereco1_idx` (`fk_endereco_evento` ASC) VISIBLE,
  INDEX `fk_evento_status_evento1_idx` (`fk_status_evento` ASC) VISIBLE,
  INDEX `fk_evento_publico_alvo1_idx` (`fk_publico_alvo_evento` ASC) VISIBLE,
  INDEX `fk_evento_categoria1_idx` (`fk_categoria_evento` ASC) VISIBLE,
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
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`evento` (id_evento, nome_evento, descricao, dia, hora_inicio, hora_fim, is_aberto, qtd_vaga, qtd_interessado, fk_endereco_evento, fk_status_evento, fk_publico_alvo_evento, fk_categoria_evento)
VALUES (1, 'Workshop de Tecnologia', 'Aprenda sobre novas tecnologias.', '2025-05-10', '09:00:00', '17:00:00', 1, 50, 30, 1, 1, 1, 1);

-- -----------------------------------------------------
-- Table `mocuti`.`status_inscricao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`status_inscricao` (
  `id_inscricao` INT NOT NULL,
  `tipo_inscricao` VARCHAR(45) NULL,
  PRIMARY KEY (`id_inscricao`))
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`status_inscricao` (id_inscricao, tipo_inscricao)
VALUES (1, 'Inscrito');

-- -----------------------------------------------------
-- Table `mocuti`.`participacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`participacao` (
  `fk_usuario_participacao` INT NOT NULL,
  `fk_evento_participacao` INT NOT NULL,
  `is_inscrito` TINYINT NULL,
  `is_presente` TINYINT NULL,
  `fk_inscricao_participacao` INT NOT NULL,
  PRIMARY KEY (`fk_usuario_participacao`, `fk_evento_participacao`),
  INDEX `fk_usuario_has_evento_evento1_idx` (`fk_evento_participacao` ASC) VISIBLE,
  INDEX `fk_usuario_has_evento_usuario_idx` (`fk_usuario_participacao` ASC) VISIBLE,
  INDEX `fk_participacao_status_inscricao1_idx` (`fk_inscricao_participacao` ASC) VISIBLE,
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
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`participacao` (fk_usuario_participacao, fk_evento_participacao, is_inscrito, is_presente, fk_inscricao_participacao)
VALUES (1, 1, 1, 0, 1);

-- -----------------------------------------------------
-- Table `mocuti`.`feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`feedback` (
  `id_feedbak` INT NOT NULL,
  `nota` INT NULL,
  `comentario` VARCHAR(255) NULL,
  `fk_evento_feedback` INT NOT NULL,
  `fk_usuario_feedback` INT NOT NULL,
  PRIMARY KEY (`id_feedbak`),
  INDEX `fk_feedbak_evento1_idx` (`fk_evento_feedback` ASC) VISIBLE,
  INDEX `fk_feedback_usuario1_idx` (`fk_usuario_feedback` ASC) VISIBLE,
  CONSTRAINT `fk_feedbak_evento1`
    FOREIGN KEY (`fk_evento_feedback`)
    REFERENCES `mocuti`.`evento` (`id_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_feedback_usuario1`
    FOREIGN KEY (`fk_usuario_feedback`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`feedback` (id_feedbak, nota, comentario, fk_evento_feedback, fk_usuario_feedback)
VALUES (1, 5, 'Evento maravilhoso!', 1, 1);

-- -----------------------------------------------------
-- Table `mocuti`.`preferencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`preferencia` (
  `id_preferencia` INT NOT NULL,
  `fk_usuario_preferencia` INT NOT NULL,
  `fk_categoria_preferencia` INT NOT NULL,
  PRIMARY KEY (`id_preferencia`, `fk_usuario_preferencia`, `fk_categoria_preferencia`),
  INDEX `fk_usuario_has_categoria_categoria1_idx` (`fk_categoria_preferencia` ASC) VISIBLE,
  INDEX `fk_usuario_has_categoria_usuario1_idx` (`fk_usuario_preferencia` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_has_categoria_usuario1`
    FOREIGN KEY (`fk_usuario_preferencia`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_categoria_categoria1`
    FOREIGN KEY (`fk_categoria_preferencia`)
    REFERENCES `mocuti`.`categoria` (`id_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- INSERT exemplo:
INSERT INTO `mocuti`.`preferencia` (id_preferencia, fk_usuario_preferencia, fk_categoria_preferencia)
VALUES (1, 1, 1);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
