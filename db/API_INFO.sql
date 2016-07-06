CREATE TABLE `api_info` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`api_name` VARCHAR(200) NOT NULL,
	`ret_result` LONGTEXT NOT NULL,
	`status` INT(11) NOT NULL DEFAULT '1',
	`created_at` DATETIME NOT NULL,
	`created_by` VARCHAR(50) NOT NULL DEFAULT 'sys',
	`updated_at` DATETIME NOT NULL,
	`updated_by` VARCHAR(50) NOT NULL DEFAULT 'sys',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `api_name` (`api_name`)
)
ENGINE=InnoDB
AUTO_INCREMENT=4;
