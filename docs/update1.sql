ALTER TABLE `dispatching_office`.`order` RENAME TO  `dispatching_office`.`orders` ;
ALTER TABLE `dispatching_office`.`order_status` ADD COLUMN `color` VARCHAR(45) NULL COMMENT '' AFTER `title`;


