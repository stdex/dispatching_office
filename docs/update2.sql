ALTER TABLE `dispatching_office`.`workers` ADD COLUMN `blacklist` INT(11) NULL COMMENT '' AFTER `count_notperformed`;
ALTER TABLE `dispatching_office`.`orders` ADD COLUMN `region` INT(11) NULL COMMENT '' AFTER `archive`;
ALTER TABLE `dispatching_office`.`orders` ADD COLUMN `start_time` TEXT NULL COMMENT '' AFTER `region`, ADD COLUMN `end_time` TEXT NULL COMMENT '' AFTER `start_time`;

ALTER TABLE `dispatching_office`.`orders` 
ADD INDEX `fk_orders_1_idx` (`region` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`orders` 
ADD CONSTRAINT `fk_orders_1`
  FOREIGN KEY (`region`)
  REFERENCES `dispatching_office`.`workers_regions` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`orders` 
ADD COLUMN `price` TEXT NULL COMMENT '' AFTER `end_time`;


ALTER TABLE `dispatching_office`.`orders` 
ADD COLUMN `annotation` TEXT NULL COMMENT '' AFTER `price`;

ALTER TABLE `dispatching_office`.`workers` 
ADD COLUMN `annotation` TEXT NULL COMMENT '' AFTER `blacklist`;

ALTER TABLE `dispatching_office`.`workers` 
ADD COLUMN `photo` BLOB NULL COMMENT '' AFTER `annotation`;



ALTER TABLE `dispatching_office`.`workers` 
DROP FOREIGN KEY `fk_workers_1`;
ALTER TABLE `dispatching_office`.`workers` 
CHANGE COLUMN `specialization` `specialization` TEXT NULL DEFAULT NULL COMMENT '' ,
DROP INDEX `fk_workers_1_idx` ;
ALTER TABLE `dispatching_office`.`workers` 
ADD CONSTRAINT `fk_workers_1`
  FOREIGN KEY (`specialization`)
  REFERENCES `dispatching_office`.`workers_specialization` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`workers` 
DROP INDEX `fk_workers_1_idx` ;


ALTER TABLE `dispatching_office`.`workers` 
DROP FOREIGN KEY `fk_workers_1`;
ALTER TABLE `dispatching_office`.`workers` 
CHANGE COLUMN `specialization` `specialization` TEXT NULL DEFAULT NULL COMMENT '' ;
ALTER TABLE `dispatching_office`.`workers` 
ADD CONSTRAINT `fk_workers_1`
  FOREIGN KEY (`specialization`)
  REFERENCES `dispatching_office`.`workers_specialization` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
ALTER TABLE `dispatching_office`.`workers` 
CHANGE COLUMN `specialization` `specialization` TEXT NULL DEFAULT NULL COMMENT '' ;

CREATE TABLE `dispatching_office`.`workers_reaction` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `title` VARCHAR(255) NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');

INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('1', 'не взял заказ');
INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('2', 'был недоступен');
INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('3', 'назначен на заказ');
INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('4', 'замена мастера');
INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('5', 'отказ');
INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('6', 'выполнен');
INSERT INTO `dispatching_office`.`workers_reaction` (`id`, `title`) VALUES ('7', 'добавлен в черный список');


CREATE TABLE `dispatching_office`.`workers_timeline` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `id_user` INT NULL COMMENT '',
  `id_worker` INT NULL COMMENT '',
  `id_order` INT NULL COMMENT '',
  `id_reaction` INT NULL COMMENT '',
  `reason` TEXT NULL COMMENT '',
  `dt` TIMESTAMP NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');

ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD INDEX `fk_workers_timeline_1_idx` (`id_user` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD CONSTRAINT `fk_workers_timeline_1`
  FOREIGN KEY (`id_user`)
  REFERENCES `dispatching_office`.`users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  
ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD INDEX `fk_workers_timeline_2_idx` (`id_worker` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD CONSTRAINT `fk_workers_timeline_2`
  FOREIGN KEY (`id_worker`)
  REFERENCES `dispatching_office`.`workers` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD INDEX `fk_workers_timeline_3_idx` (`id_order` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD CONSTRAINT `fk_workers_timeline_3`
  FOREIGN KEY (`id_order`)
  REFERENCES `dispatching_office`.`orders` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD INDEX `fk_workers_timeline_4_idx` (`id_reaction` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`workers_timeline` 
ADD CONSTRAINT `fk_workers_timeline_4`
  FOREIGN KEY (`id_reaction`)
  REFERENCES `dispatching_office`.`workers_reaction` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


CREATE TABLE `dispatching_office`.`orders_timeline` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `id_order` INT NULL COMMENT '',
  `id_status` INT NULL COMMENT '',
  `id_worker` INT NULL COMMENT '',
  `id_user` INT NULL COMMENT '',
  `dt` TIMESTAMP NULL COMMENT '',
  `reason` TEXT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');

ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD INDEX `fk_orders_timeline_1_idx` (`id_user` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD CONSTRAINT `fk_orders_timeline_1`
  FOREIGN KEY (`id_user`)
  REFERENCES `dispatching_office`.`users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD INDEX `fk_orders_timeline_2_idx` (`id_order` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD CONSTRAINT `fk_orders_timeline_2`
  FOREIGN KEY (`id_order`)
  REFERENCES `dispatching_office`.`orders` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD INDEX `fk_orders_timeline_3_idx` (`id_status` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD CONSTRAINT `fk_orders_timeline_3`
  FOREIGN KEY (`id_status`)
  REFERENCES `dispatching_office`.`order_status` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD INDEX `fk_orders_timeline_4_idx` (`id_worker` ASC)  COMMENT '';
ALTER TABLE `dispatching_office`.`orders_timeline` 
ADD CONSTRAINT `fk_orders_timeline_4`
  FOREIGN KEY (`id_worker`)
  REFERENCES `dispatching_office`.`workers` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dispatching_office`.`orders_timeline` 
RENAME TO  `dispatching_office`.`order_timeline` ;


ALTER TABLE `dispatching_office`.`workers` 
ADD COLUMN `summ_price` INT(11) NULL COMMENT '' AFTER `photo`;


ALTER TABLE `dispatching_office`.`workers` 
DROP FOREIGN KEY `fk_workers_2`;

ALTER TABLE `dispatching_office`.`orders` 
DROP FOREIGN KEY `fk_orders_1`;

ALTER TABLE `dispatching_office`.`workers` 
DROP INDEX `fk_workers_2_idx` ;

ALTER TABLE `dispatching_office`.`workers` 
CHANGE COLUMN `region` `region` TEXT NULL DEFAULT NULL COMMENT '' ;
