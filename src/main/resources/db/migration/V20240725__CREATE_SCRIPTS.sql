CREATE TABLE `tbl_employee` (
  `emp_id` varchar(255) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
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
  `mail` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_verify` varchar(255) DEFAULT 'FALSE',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tbl_admin` (`emp_id`, `first_name`, `last_name`, `location`, `mail`, `password`, `role`, `status`)
VALUES 
('V00155', 'Gauri', 'Khalate', 'Pune', 'gauri.khalate@verinite.com', '$2a$10$WoCEnk6ZBoSW3FrjBTaKT.LIg/1T57USmRZYQPA.Okfc6Mizpqi1C', 'admin', 'Active'),
('V00557', 'Soundararajan', 'S', 'Pune', 'soundararajan.s@verinite.com', '$2a$10$DMdRMpQ4KpF400DSngiHfu/SaOusIS2OyiDMQJfzZgqyHVK4JGmMu', 'admin', 'Active');


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
  `loc_code` int DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `model_name` varchar(255) DEFAULT NULL,
  `operating_system` varchar(255) DEFAULT NULL,
  `purchase_date` varchar(255) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `serial_number` varchar(255)  UNIQUE DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `warranty_date` varchar(255) DEFAULT NULL,
  `asset_sourced_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`asset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assets_history` (
  `history_id` bigint NOT NULL,
  `assigned_by` varchar(255) DEFAULT NULL,
  `assigned_date` datetime(6) DEFAULT NULL,
  `emp_id` varchar(255) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_assigned_assets` (
  `assigned_assets_id` int NOT NULL,
  `added_by` varchar(255) DEFAULT NULL,
  `asset_id` int DEFAULT NULL,
  `asset_name` varchar(255) DEFAULT NULL,
  `assigned_by` varchar(255) DEFAULT NULL,
  `assigned_date` datetime(6) DEFAULT NULL,
  `emp_id` varchar(255) DEFAULT NULL,
  `loc_code` int NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `model_name` varchar(255) DEFAULT NULL,
  `operating_system` varchar(255) DEFAULT NULL,
  `purchase_date` varchar(255) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `warranty_date` varchar(255) DEFAULT NULL,
  `asset_sourced_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`assigned_assets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_count` (
  `location` varchar(255) NOT NULL,
  `bag_count` int NOT NULL,
  `camera_count` int NOT NULL,
  `data_card_count` int NOT NULL,
  `dvr_count` int NOT NULL,
  `fire_wall_count` int NOT NULL,
  `head_phones_count` int NOT NULL,
  `laptop_charger_count` int NOT NULL,
  `laptop_count` int NOT NULL,
  `mobile_count` int NOT NULL,
  `mouse_count` int NOT NULL,
  `projector_count` int NOT NULL,
  `speaker_count` int NOT NULL,
  `switch_count` int NOT NULL,
  `un_assigned_bag_count` int NOT NULL,
  `un_assigned_camera_count` int NOT NULL,
  `un_assigned_data_card_count` int NOT NULL,
  `un_assigned_dvr_count` int NOT NULL,
  `un_assigned_fire_wall_count` int NOT NULL,
  `un_assigned_headphones_count` int NOT NULL,
  `un_assigned_laptop_charger_count` int NOT NULL,
  `un_assigned_laptop_count` int NOT NULL,
  `un_assigned_mobile_count` int NOT NULL,
  `un_assigned_mouse_count` int NOT NULL,
  `un_assigned_projector_count` int NOT NULL,
  `un_assigned_speaker_count` int NOT NULL,
  `un_assigned_switch_count` int NOT NULL,
  PRIMARY KEY (`location`)
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


