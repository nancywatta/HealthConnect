USE hc;

CREATE TABLE `ACCOUNT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(64) NOT NULL,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;

CREATE TABLE `GROUP` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(64) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;

CREATE TABLE `MEMBER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mem_acc_fk` (`account_id`),
  KEY `mem_group_fk` (`group_id`),
  CONSTRAINT `mem_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  CONSTRAINT `mem_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;


CREATE TABLE `APPOINTMENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `time` datetime NOT NULL,
  `location` varchar(64) DEFAULT NULL,
  `desciption` varchar(256) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `isShared` varchar(4) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;

CREATE TABLE `APP_ACC_REF` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `appaccref_acc_fk` (`account_id`),
  KEY `appaccref_app_fk` (`appointment_id`),
  CONSTRAINT `appaccref_app_fk` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
  CONSTRAINT `appaccref_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;


CREATE TABLE `TASK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `execute_time` datetime DEFAULT NULL,
  `isValid` varchar(4) DEFAULT NULL,
  `isShared` varchar(4) DEFAULT NULL,
  `desciption` varchar(256) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `task_acc_fk` (`account_id`),
  CONSTRAINT `task_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;


CREATE TABLE `EVENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_type` varchar(4) NOT NULL,
  `event_content` varchar(256) DEFAULT NULL,
  `send_to` varchar(256) DEFAULT NULL,
  `send_type` varchar(4) NOT NULL,
  `send_time_scheduled` datetime DEFAULT NULL,
  `send_time_actural` datetime DEFAULT NULL,
  `send_result_type` varchar(4)  DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;


CREATE TABLE `DICTIONARY` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `value` varchar(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;

CREATE TABLE `APN_USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
