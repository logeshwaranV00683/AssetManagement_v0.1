CREATE TABLE `tbl_employee` (
  `emp_id` varchar(10) NOT NULL,
  `department` varchar(30) NOT NULL,
  `designation` varchar(30) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) DEFAULT NULL,
  `location` varchar(30) NOT NULL,
  `mail` varchar(50) NOT NULL UNIQUE,
  `mobile` varchar(10) NOT NULL UNIQUE,
  `role` varchar(30) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_admin` (
  `admin_id` bigint NOT NULL AUTO_INCREMENT,
  `emp_id` varchar(10) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) DEFAULT NULL,
  `location` varchar(30) NOT NULL,
  `mail` varchar(50) NOT NULL UNIQUE,
  `password` varchar(100) NOT NULL,
  `role` varchar(30) NOT NULL,
  `status` varchar(30) NOT NULL,
  `otp` varchar(10) DEFAULT NULL,
  `otp_verify` varchar(10) DEFAULT 'FALSE',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tbl_admin` (`emp_id`, `first_name`, `last_name`, `location`, `mail`, `password`, `role`, `status`)
VALUES ('V00000', 'admin', '', 'Pune', 'logeshwaran.s@verinite.com', '$2a$10$.KSwveJZ.bG2f4FsPfWe7eavi9cEjj305v6RzDyh/yAEfCbd46cfS', 'admin', 'Active');

CREATE TABLE `tbl_assets` (
  `asset_id` int NOT NULL AUTO_INCREMENT,
  `added_by` varchar(10) NOT NULL,
  `asset_name` varchar(50) NOT NULL,
  `assigned_by` varchar(10) DEFAULT NULL,
  `assigned_date` DATE DEFAULT NULL,
  `emp_id` varchar(10) DEFAULT NULL,
  `location` varchar(30) NOT NULL,
  `model_name` varchar(50) DEFAULT NULL,
  `operating_system` varchar(30) DEFAULT NULL,
  `purchase_date` DATE NOT NULL,
  `return_date` DATE DEFAULT NULL,
  `serial_number` varchar(50)  UNIQUE NOT NULL,
  `status` varchar(30) NOT NULL,
  `type` varchar(30) NOT NULL,
  `warranty_date` DATE DEFAULT NULL,
  `asset_sourced_by` varchar(30) NOT NULL,
  PRIMARY KEY (`asset_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assets_history` (
  `history_id` bigint NOT NULL AUTO_INCREMENT,
  `assigned_by` varchar(10) NOT NULL,
  `assigned_date` DATE NOT NULL,
  `emp_id` varchar(10) NOT NULL,
  `return_date` DATE DEFAULT NULL,
  `serial_number` varchar(50) NOT NULL,
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assigned_assets` (
  `assigned_assets_id` int NOT NULL AUTO_INCREMENT,
  `asset_name` varchar(50) NOT NULL,
  `assigned_by` varchar(10) NOT NULL,
  `assigned_date` DATE NOT NULL,
  `emp_id` varchar(10) NOT NULL,
  `serial_number` varchar(50) NOT NULL,
  `status` varchar(30) NOT NULL,
  `type` varchar(30) NOT NULL,
  PRIMARY KEY (`assigned_assets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_count` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(30) NOT NULL,
  `type` VARCHAR(30) NOT NULL,
  `total` INT NOT NULL DEFAULT 0,
  `assigned` INT NOT NULL DEFAULT 0,
  `unassigned` INT NOT NULL DEFAULT 0,
  `scrapped` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_location_type` (`location`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_deleted_asset` (
  `serial_no` VARCHAR(50) NOT NULL,
  `asset_name` VARCHAR(50) NOT NULL,
  `purchase_date` DATE NOT NULL,
  `deleted_date` DATE NOT NULL,
  `type` VARCHAR(30) NOT NULL,
  `location` VARCHAR(30) NOT NULL,
  `asset_sourced_by` VARCHAR(30) NOT NULL,
  `deleted_by` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`serial_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;