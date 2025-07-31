CREATE TABLE `tbl_employee` (
  `emp_id` varchar(255) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL UNIQUE,
  `mobile` varchar(255) DEFAULT NULL UNIQUE,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_admin` (
  `admin_id` bigint NOT NULL AUTO_INCREMENT,
  `emp_id` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_verify` varchar(255) DEFAULT 'FALSE',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tbl_admin` (`emp_id`, `first_name`, `last_name`, `location`, `mail`, `password`, `role`, `status`)
VALUES ('V00000', 'admin', '', 'Pune', 'logeshwaran.s@verinite.com', '$2a$10$.KSwveJZ.bG2f4FsPfWe7eavi9cEjj305v6RzDyh/yAEfCbd46cfS', 'admin', 'Active');


CREATE TABLE `asset_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asset_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `designation` (
  `desc_id` bigint NOT NULL,
  `position` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`desc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_asset_names` (
  `asset_name_id` bigint NOT NULL,
  `asset_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`asset_name_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assets` (
  `asset_id` int NOT NULL AUTO_INCREMENT,
  `added_by` varchar(255) DEFAULT NULL,
  `asset_name` varchar(255) DEFAULT NULL,
  `assigned_by` varchar(255) DEFAULT NULL,
  `assigned_date` datetime(6) DEFAULT NULL,
  `emp_id` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `model_name` varchar(255) DEFAULT NULL,
  `operating_system` varchar(255) DEFAULT NULL,
  `purchase_date` datetime(6) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `serial_number` varchar(255)  UNIQUE DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `warranty_date` datetime(6) DEFAULT NULL,
  `asset_sourced_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`asset_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assets_history` (
  `history_id` bigint NOT NULL AUTO_INCREMENT,
  `assigned_by` varchar(255) DEFAULT NULL,
  `assigned_date` datetime(6) DEFAULT NULL,
  `emp_id` varchar(255) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assigned_assets` (
  `assigned_assets_id` int NOT NULL AUTO_INCREMENT,
  `asset_name` varchar(255) DEFAULT NULL,
  `assigned_by` varchar(255) DEFAULT NULL,
  `assigned_date` datetime(6) DEFAULT NULL,
  `emp_id` varchar(255) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`assigned_assets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_count` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(255) NOT NULL,
  `type` VARCHAR(100) NOT NULL,
  `total` INT NOT NULL DEFAULT 0,
  `assigned` INT NOT NULL DEFAULT 0,
  `unassigned` INT NOT NULL DEFAULT 0,
  `scrapped` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_location_type` (`location`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `tbl_location` (
  `loc_code` int NOT NULL AUTO_INCREMENT,
  `country` varchar(255) NOT NULL,
  `loc_name` varchar(255) DEFAULT NULL,
  `state` varchar(255) NOT NULL,
  PRIMARY KEY (`loc_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_scraptable` (
  `scrap_id` int NOT NULL AUTO_INCREMENT,
  `assetname` varchar(255) DEFAULT NULL,
  `serial_no` varchar(255) DEFAULT NULL,
  `asset_id` int NOT NULL,
  `purchase_date` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `users` varchar(255) DEFAULT NULL,
  `warranty_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`scrap_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


