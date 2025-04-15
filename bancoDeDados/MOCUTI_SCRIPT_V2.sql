-- MySQL Script atualizado
-- Model: New Model    Version: 1.0
-- Alterado por ChatGPT em 2025-04-15

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
  PRIMARY KEY (`id_endereco`)
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`endereco` VALUES
(1, '01001000', 'Av. Paulista', 1000, 'Apto 101', 'SP', 'São Paulo', 'Bela Vista');

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
  `fk_endereco_usuario` INT NOT NULL,
  `is_ativo` TINYINT NULL,
  `dt_desativacao` DATE NULL,
  PRIMARY KEY (`id_usuario`),
  INDEX `fk_usuario_endereco1_idx` (`fk_endereco_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_endereco1`
    FOREIGN KEY (`fk_endereco_usuario`)
    REFERENCES `mocuti`.`endereco` (`id_endereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`usuario` VALUES
(1, 'Ana Souza', '123.456.789-00', '11999999999', 'ana@example.com', '1995-08-15', 'Feminino', 'senha123', 1, 1, NULL);

-- -----------------------------------------------------
-- Table `mocuti`.`evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`evento` (
  `id_evento` INT NOT NULL,
  `nome_evento` VARCHAR(45) NULL,
  `descricao` VARCHAR(255) NULL,
  `status` TINYINT NULL,
  `dia` DATE NULL,
  `hora_inicio` TIME NULL,
  `hora_fim` TIME NULL,
  `publico_alvo` VARCHAR(45) NULL,
  `is_aberto` TINYINT NULL,
  `qtd_vaga` INT NULL,
  `qtd_interessado` INT NULL,
  `foto` MEDIUMBLOB NULL,
  `fk_endereco_evento` INT NOT NULL,
  PRIMARY KEY (`id_evento`),
  INDEX `fk_evento_endereco1_idx` (`fk_endereco_evento` ASC) VISIBLE,
  CONSTRAINT `fk_evento_endereco1`
    FOREIGN KEY (`fk_endereco_evento`)
    REFERENCES `mocuti`.`endereco` (`id_endereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`evento` (`id_evento`, `nome_evento`, `descricao`, `status`, `dia`, `hora_inicio`, `hora_fim`, `publico_alvo`, `is_aberto`, `qtd_vaga`, `qtd_interessado`, `foto`, `fk_endereco_evento`)
VALUES (1, 'Oficina de Artesanato', 'Aprenda técnicas básicas de artesanato.', 1, '2025-05-10', '14:00:00', '17:00:00', 'Comunidade local', 1, 30, 15, NULL, 1);

-- -----------------------------------------------------
-- Table `mocuti`.`participacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`participacao` (
  `fk_usuario_participacao` INT NOT NULL,
  `fk_evento_participacao` INT NOT NULL,
  `is_inscrito` TINYINT NULL,
  `is_presente` TINYINT NULL,
  PRIMARY KEY (`fk_usuario_participacao`, `fk_evento_participacao`),
  INDEX `fk_usuario_has_evento_evento1_idx` (`fk_evento_participacao` ASC) VISIBLE,
  INDEX `fk_usuario_has_evento_usuario_idx` (`fk_usuario_participacao` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_has_evento_usuario`
    FOREIGN KEY (`fk_usuario_participacao`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_evento_evento1`
    FOREIGN KEY (`fk_evento_participacao`)
    REFERENCES `mocuti`.`evento` (`id_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`participacao` VALUES
(1, 1, 1, 0);

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
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`feedback` VALUES
(1, 5, 'Evento muito bom!', 1, 1);

-- -----------------------------------------------------
-- Table `mocuti`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`categoria` (
  `id_categoria` INT NOT NULL,
  `nome` VARCHAR(125) NULL,
  `descricao` VARCHAR(255) NULL,
  `fk_evento_categoria` INT NOT NULL,
  PRIMARY KEY (`id_categoria`),
  INDEX `fk_categoria_evento1_idx` (`fk_evento_categoria` ASC) VISIBLE,
  CONSTRAINT `fk_categoria_evento1`
    FOREIGN KEY (`fk_evento_categoria`)
    REFERENCES `mocuti`.`evento` (`id_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`categoria` VALUES
(1, 'Cultura', 'Eventos relacionados à cultura local', 1);

-- -----------------------------------------------------
-- Table `mocuti`.`cargo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`cargo` (
  `id_cargo` INT NOT NULL,
  `tipo` VARCHAR(45) NULL,
  `usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_cargo`),
  INDEX `fk_cargo_usuario1_idx` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_cargo_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `mocuti`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`cargo` VALUES
(1, 'Voluntário', 1);

-- -----------------------------------------------------
-- Table `mocuti`.`presenca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mocuti`.`presenca` (
  `id_presenca` INT NOT NULL,
  `is_presente` TINYINT NULL,
  `participacao_fk_usuario_participacao` INT NOT NULL,
  `participacao_fk_evento_participacao` INT NOT NULL,
  PRIMARY KEY (`id_presenca`),
  INDEX `fk_presenca_participacao1_idx` (`participacao_fk_usuario_participacao` ASC, `participacao_fk_evento_participacao` ASC) VISIBLE,
  CONSTRAINT `fk_presenca_participacao1`
    FOREIGN KEY (`participacao_fk_usuario_participacao` , `participacao_fk_evento_participacao`)
    REFERENCES `mocuti`.`participacao` (`fk_usuario_participacao` , `fk_evento_participacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `mocuti`.`presenca` VALUES
(1, 1, 1, 1);

-- Restore settings
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
