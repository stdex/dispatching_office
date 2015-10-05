CREATE DATABASE `dispatching_office` /*!40100 DEFAULT CHARACTER SET utf8 */;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dispatching_office
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dispatching_office
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dispatching_office` DEFAULT CHARACTER SET utf8 ;
USE `dispatching_office` ;

-- -----------------------------------------------------
-- Table `dispatching_office`.`order_advert`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`order_advert` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`order_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`order_category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`order_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`order_status` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `color` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`users_groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`users_groups` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `group_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `login` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `password` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `fullname` TEXT NULL DEFAULT NULL COMMENT '',
  `group_id` INT(11) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_users_1_idx` (`group_id` ASC)  COMMENT '',
  CONSTRAINT `fk_users_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `dispatching_office`.`users_groups` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`workers_regions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`workers_regions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`workers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`workers` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `fullname` TEXT NULL DEFAULT NULL COMMENT '',
  `phone` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `region` INT(11) NULL DEFAULT NULL COMMENT '',
  `specialization` TEXT NULL DEFAULT NULL COMMENT '',
  `sub_specialization` TEXT NULL DEFAULT NULL COMMENT '',
  `count_done` INT(11) NULL DEFAULT NULL COMMENT '',
  `count_inwork` INT(11) NULL DEFAULT NULL COMMENT '',
  `count_wasunavailable` INT(11) NULL DEFAULT NULL COMMENT '',
  `count_waschanged` INT(11) NULL DEFAULT NULL COMMENT '',
  `count_notperformed` INT(11) NULL DEFAULT NULL COMMENT '',
  `blacklist` INT(11) NULL DEFAULT NULL COMMENT '',
  `annotation` TEXT NULL DEFAULT NULL COMMENT '',
  `photo` BLOB NULL DEFAULT NULL COMMENT '',
  `summ_price` INT(11) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_workers_2_idx` (`region` ASC)  COMMENT '',
  CONSTRAINT `fk_workers_2`
    FOREIGN KEY (`region`)
    REFERENCES `dispatching_office`.`workers_regions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`orders` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `fullname` TEXT NULL DEFAULT NULL COMMENT '',
  `phone` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `address` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `advert_source` INT(11) NULL DEFAULT NULL COMMENT '',
  `reason` TEXT NULL DEFAULT NULL COMMENT '',
  `work_cat` INT(11) NULL DEFAULT NULL COMMENT '',
  `link_worker` INT(11) NULL DEFAULT NULL COMMENT '',
  `work_datetime` TIMESTAMP NULL DEFAULT NULL COMMENT '',
  `append_datetime` TIMESTAMP NULL DEFAULT NULL COMMENT '',
  `status` INT(11) NULL DEFAULT NULL COMMENT '',
  `link_user_id` INT(11) NULL DEFAULT NULL COMMENT '',
  `archive` INT(11) NULL DEFAULT NULL COMMENT '',
  `region` INT(11) NULL DEFAULT NULL COMMENT '',
  `start_time` TEXT NULL DEFAULT NULL COMMENT '',
  `end_time` TEXT NULL DEFAULT NULL COMMENT '',
  `price` TEXT NULL DEFAULT NULL COMMENT '',
  `annotation` TEXT NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_order_1_idx` (`link_user_id` ASC)  COMMENT '',
  INDEX `fk_order_2_idx` (`status` ASC)  COMMENT '',
  INDEX `fk_order_3_idx` (`link_worker` ASC)  COMMENT '',
  INDEX `fk_order_4_idx` (`advert_source` ASC)  COMMENT '',
  INDEX `fk_order_5_idx` (`work_cat` ASC)  COMMENT '',
  INDEX `fk_orders_1_idx` (`region` ASC)  COMMENT '',
  CONSTRAINT `fk_orders_1`
    FOREIGN KEY (`region`)
    REFERENCES `dispatching_office`.`workers_regions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_1`
    FOREIGN KEY (`link_user_id`)
    REFERENCES `dispatching_office`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_2`
    FOREIGN KEY (`status`)
    REFERENCES `dispatching_office`.`order_status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_3`
    FOREIGN KEY (`link_worker`)
    REFERENCES `dispatching_office`.`workers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_4`
    FOREIGN KEY (`advert_source`)
    REFERENCES `dispatching_office`.`order_advert` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_5`
    FOREIGN KEY (`work_cat`)
    REFERENCES `dispatching_office`.`order_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`order_timeline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`order_timeline` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `id_order` INT(11) NULL DEFAULT NULL COMMENT '',
  `id_status` INT(11) NULL DEFAULT NULL COMMENT '',
  `id_worker` INT(11) NULL DEFAULT NULL COMMENT '',
  `id_user` INT(11) NULL DEFAULT NULL COMMENT '',
  `dt` TIMESTAMP NULL DEFAULT NULL COMMENT '',
  `reason` TEXT NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_orders_timeline_1_idx` (`id_user` ASC)  COMMENT '',
  INDEX `fk_orders_timeline_2_idx` (`id_order` ASC)  COMMENT '',
  INDEX `fk_orders_timeline_3_idx` (`id_status` ASC)  COMMENT '',
  INDEX `fk_orders_timeline_4_idx` (`id_worker` ASC)  COMMENT '',
  CONSTRAINT `fk_orders_timeline_1`
    FOREIGN KEY (`id_user`)
    REFERENCES `dispatching_office`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_timeline_2`
    FOREIGN KEY (`id_order`)
    REFERENCES `dispatching_office`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_timeline_3`
    FOREIGN KEY (`id_status`)
    REFERENCES `dispatching_office`.`order_status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_timeline_4`
    FOREIGN KEY (`id_worker`)
    REFERENCES `dispatching_office`.`workers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`workers_reaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`workers_reaction` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`workers_specialization`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`workers_specialization` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`workers_sub_specialization`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`workers_sub_specialization` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `main_specialization` INT(11) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_workers_sub_specialization_1_idx` (`main_specialization` ASC)  COMMENT '',
  CONSTRAINT `fk_workers_sub_specialization_1`
    FOREIGN KEY (`main_specialization`)
    REFERENCES `dispatching_office`.`workers_specialization` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dispatching_office`.`workers_timeline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dispatching_office`.`workers_timeline` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `id_user` INT(11) NULL DEFAULT NULL COMMENT '',
  `id_worker` INT(11) NULL DEFAULT NULL COMMENT '',
  `id_order` INT(11) NULL DEFAULT NULL COMMENT '',
  `id_reaction` INT(11) NULL DEFAULT NULL COMMENT '',
  `reason` TEXT NULL DEFAULT NULL COMMENT '',
  `dt` TIMESTAMP NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `fk_workers_timeline_1_idx` (`id_user` ASC)  COMMENT '',
  INDEX `fk_workers_timeline_2_idx` (`id_worker` ASC)  COMMENT '',
  INDEX `fk_workers_timeline_3_idx` (`id_order` ASC)  COMMENT '',
  INDEX `fk_workers_timeline_4_idx` (`id_reaction` ASC)  COMMENT '',
  CONSTRAINT `fk_workers_timeline_1`
    FOREIGN KEY (`id_user`)
    REFERENCES `dispatching_office`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_workers_timeline_2`
    FOREIGN KEY (`id_worker`)
    REFERENCES `dispatching_office`.`workers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_workers_timeline_3`
    FOREIGN KEY (`id_order`)
    REFERENCES `dispatching_office`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_workers_timeline_4`
    FOREIGN KEY (`id_reaction`)
    REFERENCES `dispatching_office`.`workers_reaction` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


/*
-- Query: SELECT * FROM dispatching_office.users_groups
LIMIT 0, 1000
*/
INSERT INTO `users_groups` (`id`,`group_name`) VALUES (1,'Админстратор');
INSERT INTO `users_groups` (`id`,`group_name`) VALUES (2,'Диспетчер');


/*
-- Query: SELECT * FROM dispatching_office.users
LIMIT 0, 1000
*/
INSERT INTO `users` (`id`,`login`,`password`,`fullname`,`group_id`) VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3','Тестовый администратор',1);
INSERT INTO `users` (`id`,`login`,`password`,`fullname`,`group_id`) VALUES (2,'disp','7b1111949463c32a5ae41facb8ecb5f8','Тестовый диспетчер',2);


/*
-- Query: SELECT * FROM dispatching_office.workers_reaction
LIMIT 0, 1000
*/
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (1,'не взял заказ');
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (2,'был недоступен');
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (3,'назначен на заказ');
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (4,'замена мастера');
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (5,'отказ');
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (6,'выполнен');
INSERT INTO `workers_reaction` (`id`,`title`) VALUES (7,'добавлен в черный список');


/*
-- Query: SELECT * FROM dispatching_office.order_status
LIMIT 0, 1000
*/
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (1,'В ожидании назначения мастера','#FFCC66');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (2,'Мастер назначен, ожидаем ответ заказчика','#990000');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (3,'Заказчик договорился с мастером, ожидаем начало работ','#FF9980');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (4,'Замена мастера','#CCFFFF');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (5,'Заказчик думает','#FFFF4D');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (6,'Отказ по вине мастера','#FF6666');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (7,'Отказ по вине заказчика','#330033');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (8,'Мастер на объекте, ожидаем окончание работ','#E64D4D');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (9,'Заказ выполнен, ожидаем оплаты от мастера','#E64D4D');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (10,'Заказ выполнен, оплачен','#E64D4D');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (11,'Мастер не рассчитался','#E64D4D');
INSERT INTO `order_status` (`id`,`title`,`color`) VALUES (12,'Заказчик не расчитался','#E64D4D');

