ALTER TABLE `dispatching_office`.`workers` 
DROP FOREIGN KEY `fk_workers_2`;

ALTER TABLE `dispatching_office`.`orders` 
DROP FOREIGN KEY `fk_orders_1`;

ALTER TABLE `dispatching_office`.`workers` 
DROP INDEX `fk_workers_2_idx` ;

ALTER TABLE `dispatching_office`.`workers` 
CHANGE COLUMN `region` `region` TEXT NULL DEFAULT NULL COMMENT '' ;
