# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 5.6.20)
# Database: hc
# Generation Time: 2014-10-07 07:21:21 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table ACCOUNT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACCOUNT`;

CREATE TABLE `ACCOUNT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(64) NOT NULL,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `ACCOUNT` WRITE;
/*!40000 ALTER TABLE `ACCOUNT` DISABLE KEYS */;

INSERT INTO `ACCOUNT` (`id`, `email`, `username`, `password`, `created_date`, `updated_date`, `last_login_date`, `expiration_date`)
VALUES
	(2,'sahilwatta@gmail.com','sahilwatta','sahil','2014-09-28 13:07:53',NULL,NULL,NULL),
	(3,'nancy.watta@gmail.com','nancywatta','nancy','2014-09-28 13:08:41',NULL,NULL,NULL),
	(4,'vikramkrsingh321@gmail.com','vikram','vikram','2014-09-28 13:08:45',NULL,NULL,NULL),
	(5,'tarsemwatta@gmail.com','tarsemwatta','tarsem','2014-09-28 13:08:47',NULL,NULL,NULL),
	(6,'yuan@gmail.com',NULL,'healthConnect','2014-09-28 13:14:20',NULL,NULL,NULL),
	(7,'nancywatta@gmail.com',NULL,'healthConnect','2014-09-28 13:16:53',NULL,NULL,NULL),
	(9,'ben@gmail.com','ben','eloney','2014-09-28 13:28:44',NULL,NULL,NULL),
	(10,'yalu@gmail.com','Yalu','yaluyou','2014-09-28 13:30:22',NULL,NULL,NULL),
	(13,'dummy@gmail.com','dummy','dummy','2014-09-28 14:32:07',NULL,NULL,NULL),
	(14,'dummyPatient@gmail.com','dummyPatient','dummy','2014-09-28 14:35:04',NULL,NULL,NULL),
	(17,'google1@gmail.com',NULL,'healthConnect','2014-10-02 10:56:10',NULL,NULL,NULL),
	(18,'google2@gmail.com',NULL,'healthConnect','2014-10-02 10:56:10',NULL,NULL,NULL);

/*!40000 ALTER TABLE `ACCOUNT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ACCOUNT_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACCOUNT_AUD`;

CREATE TABLE `ACCOUNT_AUD` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_pbs9xhenk5lq31uiinsgkc1t6` (`REV`),
  CONSTRAINT `FK_pbs9xhenk5lq31uiinsgkc1t6` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `ACCOUNT_AUD` WRITE;
/*!40000 ALTER TABLE `ACCOUNT_AUD` DISABLE KEYS */;

INSERT INTO `ACCOUNT_AUD` (`ID`, `REV`, `REVTYPE`, `email`, `username`, `password`, `created_date`, `updated_date`, `last_login_date`, `expiration_date`)
VALUES
	(2,15,0,'sahilwatta@gmail.com','sahilwatta','sahil','2014-09-28 13:07:53',NULL,NULL,NULL),
	(3,16,0,'nancy.watta@gmail.com','nancywatta','nancy','2014-09-28 13:08:41',NULL,NULL,NULL),
	(4,17,0,'vikramkrsingh321@gmail.com','vikram','vikram','2014-09-28 13:08:45',NULL,NULL,NULL),
	(5,18,0,'tarsemwatta@gmail.com','tarsemwatta','tarsem','2014-09-28 13:08:47',NULL,NULL,NULL),
	(6,22,0,'yuan@gmail.com',NULL,'healthConnect','2014-09-28 13:14:20',NULL,NULL,NULL),
	(7,24,0,'nancywatta@gmail.com',NULL,'healthConnect','2014-09-28 13:16:53',NULL,NULL,NULL),
	(9,25,0,'ben@gmail.com','ben','eloney','2014-09-28 13:28:44',NULL,NULL,NULL),
	(10,26,0,'yalu@gmail.com','Yalu','yaluyou','2014-09-28 13:30:22',NULL,NULL,NULL),
	(13,31,0,'dummy@gmail.com','dummy','dummy','2014-09-28 14:32:07',NULL,NULL,NULL),
	(14,35,0,'dummyPatient@gmail.com','dummyPatient','dummy','2014-09-28 14:35:04',NULL,NULL,NULL),
	(17,70,0,'google1@gmail.com',NULL,'healthConnect','2014-10-02 10:56:10',NULL,NULL,NULL),
	(18,70,0,'google2@gmail.com',NULL,'healthConnect','2014-10-02 10:56:10',NULL,NULL,NULL);

/*!40000 ALTER TABLE `ACCOUNT_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table APN_USER
# ------------------------------------------------------------

DROP TABLE IF EXISTS `APN_USER`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `APN_USER` WRITE;
/*!40000 ALTER TABLE `APN_USER` DISABLE KEYS */;

INSERT INTO `APN_USER` (`id`, `created_date`, `email`, `name`, `password`, `updated_date`, `username`)
VALUES
	(1,'2014-10-07 00:00:00','nancy.watta@gmail.com','nancy','nancy',NULL,'nancywatta');

/*!40000 ALTER TABLE `APN_USER` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table APP_ACC_REF
# ------------------------------------------------------------

DROP TABLE IF EXISTS `APP_ACC_REF`;

CREATE TABLE `APP_ACC_REF` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `appaccref_acc_fk` (`account_id`),
  KEY `appaccref_app_fk` (`appointment_id`),
  KEY `appaccref_group_fk` (`group_id`),
  CONSTRAINT `appaccref_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `appaccref_app_fk` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
  CONSTRAINT `appaccref_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `APP_ACC_REF` WRITE;
/*!40000 ALTER TABLE `APP_ACC_REF` DISABLE KEYS */;

INSERT INTO `APP_ACC_REF` (`id`, `appointment_id`, `account_id`, `group_id`, `expiration_date`)
VALUES
	(2,4,2,6,NULL),
	(3,4,4,6,NULL),
	(4,5,5,8,NULL),
	(5,5,3,8,NULL),
	(6,5,4,8,NULL),
	(7,6,5,10,NULL),
	(8,6,2,10,NULL),
	(9,6,4,10,NULL),
	(10,10,5,10,NULL),
	(11,10,3,10,NULL),
	(12,11,6,8,'2014-10-07 19:28:08'),
	(13,11,5,8,'2014-10-07 19:28:08'),
	(14,11,4,8,'2014-10-07 19:28:08');

/*!40000 ALTER TABLE `APP_ACC_REF` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table APP_ACC_REF_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `APP_ACC_REF_AUD`;

CREATE TABLE `APP_ACC_REF_AUD` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `appointment_id` bigint(20) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_8ydb8pqmq863gvfewm3ldf49k` (`REV`),
  CONSTRAINT `FK_8ydb8pqmq863gvfewm3ldf49k` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `APP_ACC_REF_AUD` WRITE;
/*!40000 ALTER TABLE `APP_ACC_REF_AUD` DISABLE KEYS */;

INSERT INTO `APP_ACC_REF_AUD` (`ID`, `REV`, `REVTYPE`, `appointment_id`, `account_id`, `group_id`, `expiration_date`)
VALUES
	(2,63,0,4,2,6,NULL),
	(3,63,0,4,4,6,NULL),
	(4,64,0,5,5,8,NULL),
	(5,64,0,5,3,8,NULL),
	(6,64,0,5,4,8,NULL),
	(7,65,0,6,5,10,NULL),
	(8,65,0,6,2,10,NULL),
	(9,65,0,6,4,10,NULL),
	(10,69,0,10,5,10,NULL),
	(11,69,0,10,3,10,NULL),
	(12,82,0,11,6,8,NULL),
	(12,85,1,11,6,8,'2014-10-07 19:28:08'),
	(13,82,0,11,5,8,NULL),
	(13,86,1,11,5,8,'2014-10-07 19:28:08'),
	(14,82,0,11,4,8,NULL),
	(14,87,1,11,4,8,'2014-10-07 19:28:08');

/*!40000 ALTER TABLE `APP_ACC_REF_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table APPOINTMENT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `APPOINTMENT`;

CREATE TABLE `APPOINTMENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `location` varchar(64) DEFAULT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `execute_time` bigint(20) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `appointment_type` varchar(64) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `shared_type` varchar(4) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_group_fk` (`group_id`),
  CONSTRAINT `app_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `APPOINTMENT` WRITE;
/*!40000 ALTER TABLE `APPOINTMENT` DISABLE KEYS */;

INSERT INTO `APPOINTMENT` (`id`, `name`, `location`, `start_time`, `end_time`, `start_date`, `end_date`, `execute_time`, `description`, `appointment_type`, `status`, `shared_type`, `group_id`, `created_date`, `updated_date`, `expiration_date`)
VALUES
	(2,'Appointment','Mercy Hospital','21:00:00','22:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','G',2,'2014-10-01 23:21:33',NULL,NULL),
	(3,'Appointment','Mercy Ascot Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','G',4,'2014-10-01 23:33:46',NULL,NULL),
	(4,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','M',6,'2014-10-01 23:34:43',NULL,NULL),
	(5,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','M',8,'2014-10-01 23:36:12',NULL,NULL),
	(6,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','M',10,'2014-10-01 23:36:46',NULL,NULL),
	(7,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','G',12,'2014-10-01 23:37:03',NULL,NULL),
	(8,'Appointment','Mercy Hospital','21:00:00','22:00:00','2014-09-29 13:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','G',8,'2014-10-02 10:15:54',NULL,NULL),
	(9,'Appointment','Mercy Auckland Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','G',8,'2014-10-02 10:30:53','2014-10-07 19:21:29',NULL),
	(10,'Appointment','University Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','M',10,'2014-10-02 10:46:18',NULL,NULL),
	(11,'Appointment','Mercy Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','M',8,'2014-10-07 19:23:30','2014-10-07 19:28:07','2014-10-07 19:28:07');

/*!40000 ALTER TABLE `APPOINTMENT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table APPOINTMENT_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `APPOINTMENT_AUD`;

CREATE TABLE `APPOINTMENT_AUD` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `location` varchar(64) DEFAULT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `execute_time` bigint(20) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `appointment_type` varchar(64) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `shared_type` varchar(4) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_quebkn23e9pvwin25f8j8mig2` (`REV`),
  CONSTRAINT `FK_quebkn23e9pvwin25f8j8mig2` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `APPOINTMENT_AUD` WRITE;
/*!40000 ALTER TABLE `APPOINTMENT_AUD` DISABLE KEYS */;

INSERT INTO `APPOINTMENT_AUD` (`ID`, `REV`, `REVTYPE`, `name`, `location`, `start_time`, `end_time`, `start_date`, `end_date`, `execute_time`, `description`, `appointment_type`, `status`, `shared_type`, `group_id`, `created_date`, `updated_date`, `expiration_date`)
VALUES
	(2,61,0,'Appointment','Mercy Hospital','21:00:00','22:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','G',2,'2014-10-01 23:21:33',NULL,NULL),
	(3,62,0,'Appointment','Mercy Ascot Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','G',4,'2014-10-01 23:33:46',NULL,NULL),
	(4,63,0,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','M',6,'2014-10-01 23:34:43',NULL,NULL),
	(5,64,0,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','M',8,'2014-10-01 23:36:12',NULL,NULL),
	(6,65,0,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','M',10,'2014-10-01 23:36:46',NULL,NULL),
	(7,66,0,'Appointment','Auckland Hospital','21:00:00','22:00:00','2014-09-29 14:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','G',12,'2014-10-01 23:37:03',NULL,NULL),
	(8,67,0,'Appointment','Mercy Hospital','21:00:00','22:00:00','2014-09-29 13:00:00','3014-09-29 13:00:00',1,'Diabetes',NULL,'A','G',8,'2014-10-02 10:15:54',NULL,NULL),
	(9,68,0,'Appointment','Mercy Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','G',8,'2014-10-02 10:30:53',NULL,NULL),
	(9,81,1,'Appointment','Mercy Auckland Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','G',8,'2014-10-02 10:30:53','2014-10-07 19:21:29',NULL),
	(10,69,0,'Appointment','University Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','M',10,'2014-10-02 10:46:18',NULL,NULL),
	(11,82,0,'Appointment','Mercy Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','M',8,'2014-10-07 19:23:30',NULL,NULL),
	(11,83,1,'Appointment','Mercy Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','M',8,'2014-10-07 19:23:30','2014-10-07 19:24:30','2014-10-07 19:24:30'),
	(11,84,1,'Appointment','Mercy Hospital','08:00:00','09:00:00','2014-09-29 00:00:00','3014-09-29 00:00:00',1,'Diabetes',NULL,'A','M',8,'2014-10-07 19:23:30','2014-10-07 19:28:07','2014-10-07 19:28:07');

/*!40000 ALTER TABLE `APPOINTMENT_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table DICTIONARY
# ------------------------------------------------------------

DROP TABLE IF EXISTS `DICTIONARY`;

CREATE TABLE `DICTIONARY` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `value` varchar(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `DICTIONARY` WRITE;
/*!40000 ALTER TABLE `DICTIONARY` DISABLE KEYS */;

INSERT INTO `DICTIONARY` (`id`, `type`, `value`, `name`, `description`)
VALUES
	(1,'Role','P','Patient','Patient'),
	(2,'Role','N','Nurse','Health Professional'),
	(3,'Role','S','Support member','Support Member');

/*!40000 ALTER TABLE `DICTIONARY` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table EVENT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `EVENT`;

CREATE TABLE `EVENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_type` varchar(4) NOT NULL,
  `event_content` varchar(256) DEFAULT NULL,
  `send_to` varchar(256) DEFAULT NULL,
  `send_type` varchar(4) NOT NULL,
  `send_time_scheduled` datetime DEFAULT NULL,
  `send_time_actural` datetime DEFAULT NULL,
  `send_result_type` varchar(4) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table GROUP_INFO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `GROUP_INFO`;

CREATE TABLE `GROUP_INFO` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(64) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `GROUP_INFO` WRITE;
/*!40000 ALTER TABLE `GROUP_INFO` DISABLE KEYS */;

INSERT INTO `GROUP_INFO` (`id`, `groupname`, `created_date`, `updated_date`, `expiration_date`)
VALUES
	(2,'group1','2014-09-28 13:10:19','2014-09-28 13:16:54',NULL),
	(4,'group2','2014-09-28 13:11:33',NULL,NULL),
	(6,'group3','2014-09-28 13:13:27',NULL,NULL),
	(7,'group4','2014-09-28 13:14:20',NULL,NULL),
	(8,'group5','2014-09-28 13:15:09',NULL,NULL),
	(9,'group6','2014-09-28 13:31:08',NULL,NULL),
	(10,'group7','2014-09-28 13:36:11',NULL,NULL),
	(11,'group8','2014-09-28 13:36:58',NULL,NULL),
	(12,'group9','2014-09-28 13:42:30',NULL,NULL),
	(15,'group10','2014-09-28 14:32:42',NULL,'2014-09-28 14:33:22'),
	(16,'group11','2014-09-28 14:35:58','2014-09-28 14:37:47',NULL),
	(18,'dummyGroup','2014-10-02 10:56:10',NULL,NULL),
	(20,'dummyGroup1','2014-10-02 10:58:48',NULL,'2014-10-02 10:59:36'),
	(22,'dummyGroup2','2014-10-02 11:00:52',NULL,NULL),
	(23,'dummyGroup3','2014-10-02 11:01:19',NULL,'2014-10-02 11:01:30'),
	(24,'dummyGroup4','2014-10-02 11:02:42','2014-10-02 11:07:34',NULL);

/*!40000 ALTER TABLE `GROUP_INFO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table GROUP_INFO_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `GROUP_INFO_AUD`;

CREATE TABLE `GROUP_INFO_AUD` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `groupname` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_3ckdh12knuv98vmwtkjnftchj` (`REV`),
  CONSTRAINT `FK_3ckdh12knuv98vmwtkjnftchj` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `GROUP_INFO_AUD` WRITE;
/*!40000 ALTER TABLE `GROUP_INFO_AUD` DISABLE KEYS */;

INSERT INTO `GROUP_INFO_AUD` (`ID`, `REV`, `REVTYPE`, `groupname`, `created_date`, `updated_date`, `expiration_date`)
VALUES
	(2,19,0,'group1','2014-09-28 13:10:19',NULL,NULL),
	(2,24,1,'group1','2014-09-28 13:10:19','2014-09-28 13:16:54',NULL),
	(4,20,0,'group2','2014-09-28 13:11:33',NULL,NULL),
	(6,21,0,'group3','2014-09-28 13:13:27',NULL,NULL),
	(7,22,0,'group4','2014-09-28 13:14:20',NULL,NULL),
	(8,23,0,'group5','2014-09-28 13:15:09',NULL,NULL),
	(9,27,0,'group6','2014-09-28 13:31:08',NULL,NULL),
	(10,28,0,'group7','2014-09-28 13:36:11',NULL,NULL),
	(11,29,0,'group8','2014-09-28 13:36:58',NULL,NULL),
	(12,30,0,'group9','2014-09-28 13:42:30',NULL,NULL),
	(15,32,0,'group10','2014-09-28 14:32:42',NULL,NULL),
	(15,34,1,'group10','2014-09-28 14:32:42',NULL,'2014-09-28 14:33:22'),
	(16,36,0,'group11','2014-09-28 14:35:58',NULL,NULL),
	(16,37,1,'group11','2014-09-28 14:35:58','2014-09-28 14:36:21',NULL),
	(16,38,1,'group11','2014-09-28 14:35:58','2014-09-28 14:37:47',NULL),
	(18,70,0,'dummyGroup','2014-10-02 10:56:10',NULL,NULL),
	(20,71,0,'dummyGroup1','2014-10-02 10:58:48',NULL,NULL),
	(20,73,1,'dummyGroup1','2014-10-02 10:58:48',NULL,'2014-10-02 10:59:36'),
	(22,74,0,'dummyGroup2','2014-10-02 11:00:52',NULL,NULL),
	(23,75,0,'dummyGroup3','2014-10-02 11:01:19',NULL,NULL),
	(23,77,1,'dummyGroup3','2014-10-02 11:01:19',NULL,'2014-10-02 11:01:30'),
	(24,78,0,'dummyGroup4','2014-10-02 11:02:42',NULL,NULL),
	(24,79,1,'dummyGroup4','2014-10-02 11:02:42','2014-10-02 11:04:49',NULL),
	(24,80,1,'dummyGroup4','2014-10-02 11:02:42','2014-10-02 11:07:34',NULL);

/*!40000 ALTER TABLE `GROUP_INFO_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table MEMBER
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MEMBER`;

CREATE TABLE `MEMBER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `isActive` varchar(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `mem_acc_fk` (`account_id`),
  KEY `mem_group_fk` (`group_id`),
  CONSTRAINT `mem_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `mem_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `MEMBER` WRITE;
/*!40000 ALTER TABLE `MEMBER` DISABLE KEYS */;

INSERT INTO `MEMBER` (`id`, `account_id`, `group_id`, `role_id`, `created_date`, `expiration_date`, `isActive`)
VALUES
	(2,3,2,1,'2014-09-28 13:10:19',NULL,'Y'),
	(3,4,2,2,'2014-09-28 13:10:20',NULL,'Y'),
	(4,5,2,3,'2014-09-28 13:10:20',NULL,'Y'),
	(5,2,2,3,'2014-09-28 13:10:20',NULL,'Y'),
	(8,4,4,1,'2014-09-28 13:11:33',NULL,'Y'),
	(9,3,4,2,'2014-09-28 13:11:33',NULL,'Y'),
	(10,5,4,3,'2014-09-28 13:11:33',NULL,'Y'),
	(11,2,4,3,'2014-09-28 13:11:34',NULL,'Y'),
	(14,3,6,1,'2014-09-28 13:13:27',NULL,'Y'),
	(15,4,6,2,'2014-09-28 13:13:27',NULL,'Y'),
	(16,2,6,2,'2014-09-28 13:13:27',NULL,'Y'),
	(17,3,7,1,'2014-09-28 13:14:20',NULL,'Y'),
	(18,6,7,2,'2014-09-28 13:14:20',NULL,'Y'),
	(19,2,7,2,'2014-09-28 13:14:20',NULL,'Y'),
	(20,5,7,3,'2014-09-28 13:14:20',NULL,'Y'),
	(21,4,8,1,'2014-09-28 13:15:09',NULL,'Y'),
	(22,6,8,2,'2014-09-28 13:15:09',NULL,'Y'),
	(23,5,8,3,'2014-09-28 13:15:09',NULL,'Y'),
	(24,3,8,3,'2014-09-28 13:15:09',NULL,'Y'),
	(25,7,2,2,'2014-09-28 13:16:53',NULL,'Y'),
	(26,10,9,1,'2014-09-28 13:31:08',NULL,'Y'),
	(27,4,9,2,'2014-09-28 13:31:08',NULL,'Y'),
	(28,5,9,3,'2014-09-28 13:31:08',NULL,'Y'),
	(29,2,9,3,'2014-09-28 13:31:08',NULL,'Y'),
	(30,3,10,1,'2014-09-28 13:36:11',NULL,'Y'),
	(31,4,10,2,'2014-09-28 13:36:11',NULL,'Y'),
	(32,5,10,3,'2014-09-28 13:36:11',NULL,'Y'),
	(33,2,10,3,'2014-09-28 13:36:11',NULL,'Y'),
	(34,3,11,2,'2014-09-28 13:36:58',NULL,'Y'),
	(35,3,12,1,'2014-09-28 13:42:30',NULL,'Y'),
	(36,4,12,2,'2014-09-28 13:42:30',NULL,'Y'),
	(41,13,15,1,'2014-09-28 14:32:42','2014-09-28 14:33:22','N'),
	(42,14,16,1,'2014-09-28 14:35:58',NULL,'Y'),
	(43,4,16,2,'2014-09-28 14:35:58','2014-09-28 14:36:21','N'),
	(44,5,16,3,'2014-09-28 14:35:58',NULL,'Y'),
	(45,2,16,3,'2014-09-28 14:35:58','2014-09-28 14:37:47','N'),
	(49,4,18,1,'2014-10-02 10:56:10',NULL,'Y'),
	(50,17,18,2,'2014-10-02 10:56:10',NULL,'Y'),
	(51,18,18,3,'2014-10-02 10:56:10',NULL,'Y'),
	(53,4,20,1,'2014-10-02 10:58:48','2014-10-02 10:59:35','N'),
	(54,17,20,2,'2014-10-02 10:58:48','2014-10-02 10:59:35','N'),
	(56,4,22,2,'2014-10-02 11:00:52',NULL,'Y'),
	(57,17,22,1,'2014-10-02 11:00:52',NULL,'Y'),
	(58,4,23,2,'2014-10-02 11:01:19','2014-10-02 11:01:30','N'),
	(59,4,24,1,'2014-10-02 11:02:42',NULL,'Y'),
	(60,17,24,2,'2014-10-02 11:02:42','2014-10-02 11:07:34','N'),
	(61,18,24,3,'2014-10-02 11:02:42','2014-10-02 11:04:49','N');

/*!40000 ALTER TABLE `MEMBER` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table MEMBER_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MEMBER_AUD`;

CREATE TABLE `MEMBER_AUD` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `isActive` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_l987374lkixp1qutcmck072ee` (`REV`),
  KEY `mem_aud_acc_fk` (`account_id`),
  KEY `mem_aud_group_fk` (`group_id`),
  CONSTRAINT `FK_l987374lkixp1qutcmck072ee` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  CONSTRAINT `mem_aud_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `mem_aud_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `MEMBER_AUD` WRITE;
/*!40000 ALTER TABLE `MEMBER_AUD` DISABLE KEYS */;

INSERT INTO `MEMBER_AUD` (`ID`, `REV`, `REVTYPE`, `account_id`, `group_id`, `role_id`, `created_date`, `expiration_date`, `isActive`)
VALUES
	(2,19,0,3,2,1,'2014-09-28 13:10:19',NULL,'Y'),
	(3,19,0,4,2,2,'2014-09-28 13:10:20',NULL,'Y'),
	(4,19,0,5,2,3,'2014-09-28 13:10:20',NULL,'Y'),
	(5,19,0,2,2,3,'2014-09-28 13:10:20',NULL,'Y'),
	(8,20,0,4,4,1,'2014-09-28 13:11:33',NULL,'Y'),
	(9,20,0,3,4,2,'2014-09-28 13:11:33',NULL,'Y'),
	(10,20,0,5,4,3,'2014-09-28 13:11:33',NULL,'Y'),
	(11,20,0,2,4,3,'2014-09-28 13:11:34',NULL,'Y'),
	(14,21,0,3,6,1,'2014-09-28 13:13:27',NULL,'Y'),
	(15,21,0,4,6,2,'2014-09-28 13:13:27',NULL,'Y'),
	(16,21,0,2,6,2,'2014-09-28 13:13:27',NULL,'Y'),
	(17,22,0,3,7,1,'2014-09-28 13:14:20',NULL,'Y'),
	(18,22,0,6,7,2,'2014-09-28 13:14:20',NULL,'Y'),
	(19,22,0,2,7,2,'2014-09-28 13:14:20',NULL,'Y'),
	(20,22,0,5,7,3,'2014-09-28 13:14:20',NULL,'Y'),
	(21,23,0,4,8,1,'2014-09-28 13:15:09',NULL,'Y'),
	(22,23,0,6,8,2,'2014-09-28 13:15:09',NULL,'Y'),
	(23,23,0,5,8,3,'2014-09-28 13:15:09',NULL,'Y'),
	(24,23,0,3,8,3,'2014-09-28 13:15:09',NULL,'Y'),
	(25,24,0,7,2,2,'2014-09-28 13:16:53',NULL,'Y'),
	(26,27,0,10,9,1,'2014-09-28 13:31:08',NULL,'Y'),
	(27,27,0,4,9,2,'2014-09-28 13:31:08',NULL,'Y'),
	(28,27,0,5,9,3,'2014-09-28 13:31:08',NULL,'Y'),
	(29,27,0,2,9,3,'2014-09-28 13:31:08',NULL,'Y'),
	(30,28,0,3,10,1,'2014-09-28 13:36:11',NULL,'Y'),
	(31,28,0,4,10,2,'2014-09-28 13:36:11',NULL,'Y'),
	(32,28,0,5,10,3,'2014-09-28 13:36:11',NULL,'Y'),
	(33,28,0,2,10,3,'2014-09-28 13:36:11',NULL,'Y'),
	(34,29,0,3,11,2,'2014-09-28 13:36:58',NULL,'Y'),
	(35,30,0,3,12,1,'2014-09-28 13:42:30',NULL,'Y'),
	(36,30,0,4,12,2,'2014-09-28 13:42:30',NULL,'Y'),
	(41,32,0,13,15,1,'2014-09-28 14:32:42',NULL,'Y'),
	(41,33,1,13,15,1,'2014-09-28 14:32:42','2014-09-28 14:33:22','N'),
	(42,36,0,14,16,1,'2014-09-28 14:35:58',NULL,'Y'),
	(43,36,0,4,16,2,'2014-09-28 14:35:58',NULL,'Y'),
	(43,37,1,4,16,2,'2014-09-28 14:35:58','2014-09-28 14:36:21','N'),
	(44,36,0,5,16,3,'2014-09-28 14:35:58',NULL,'Y'),
	(45,36,0,2,16,3,'2014-09-28 14:35:58',NULL,'Y'),
	(45,38,1,2,16,3,'2014-09-28 14:35:58','2014-09-28 14:37:47','N'),
	(49,70,0,4,18,1,'2014-10-02 10:56:10',NULL,'Y'),
	(50,70,0,17,18,2,'2014-10-02 10:56:10',NULL,'Y'),
	(51,70,0,18,18,3,'2014-10-02 10:56:10',NULL,'Y'),
	(53,71,0,4,20,1,'2014-10-02 10:58:48',NULL,'Y'),
	(53,72,1,4,20,1,'2014-10-02 10:58:48','2014-10-02 10:59:35','N'),
	(54,71,0,17,20,2,'2014-10-02 10:58:48',NULL,'Y'),
	(54,72,1,17,20,2,'2014-10-02 10:58:48','2014-10-02 10:59:35','N'),
	(56,74,0,4,22,2,'2014-10-02 11:00:52',NULL,'Y'),
	(57,74,0,17,22,1,'2014-10-02 11:00:52',NULL,'Y'),
	(58,75,0,4,23,2,'2014-10-02 11:01:19',NULL,'Y'),
	(58,76,1,4,23,2,'2014-10-02 11:01:19','2014-10-02 11:01:30','N'),
	(59,78,0,4,24,1,'2014-10-02 11:02:42',NULL,'Y'),
	(60,78,0,17,24,2,'2014-10-02 11:02:42',NULL,'Y'),
	(60,80,1,17,24,2,'2014-10-02 11:02:42','2014-10-02 11:07:34','N'),
	(61,78,0,18,24,3,'2014-10-02 11:02:42',NULL,'Y'),
	(61,79,1,18,24,3,'2014-10-02 11:02:42','2014-10-02 11:04:49','N');

/*!40000 ALTER TABLE `MEMBER_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table REVINFO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `REVINFO`;

CREATE TABLE `REVINFO` (
  `REV` int(11) NOT NULL AUTO_INCREMENT,
  `REVTSTMP` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `REVINFO` WRITE;
/*!40000 ALTER TABLE `REVINFO` DISABLE KEYS */;

INSERT INTO `REVINFO` (`REV`, `REVTSTMP`)
VALUES
	(15,1411862873116),
	(16,1411862921271),
	(17,1411862924605),
	(18,1411862926988),
	(19,1411863019629),
	(20,1411863093638),
	(21,1411863207045),
	(22,1411863260464),
	(23,1411863308939),
	(24,1411863414733),
	(25,1411864124628),
	(26,1411864222273),
	(27,1411864268415),
	(28,1411864571343),
	(29,1411864617668),
	(30,1411864950414),
	(31,1411867926766),
	(32,1411867962142),
	(33,1411868001545),
	(34,1411868001628),
	(35,1411868103610),
	(36,1411868158034),
	(37,1411868180769),
	(38,1411868266749),
	(40,1411976395951),
	(41,1411976396124),
	(42,1411976396134),
	(43,1411976396228),
	(44,1411976396238),
	(45,1411976396277),
	(46,1411977551011),
	(47,1411981179742),
	(48,1411981415150),
	(49,1411983961581),
	(50,1412121092438),
	(51,1412121123740),
	(52,1412121148235),
	(53,1412121164051),
	(54,1412121227978),
	(55,1412121274544),
	(56,1412121329885),
	(57,1412132593540),
	(58,1412154783966),
	(59,1412155613453),
	(60,1412156918854),
	(61,1412158893242),
	(62,1412159626084),
	(63,1412159683215),
	(64,1412159772704),
	(65,1412159806631),
	(66,1412159823347),
	(67,1412198154142),
	(68,1412199052890),
	(69,1412199978463),
	(70,1412200569973),
	(71,1412200727912),
	(72,1412200775511),
	(73,1412200775684),
	(74,1412200851830),
	(75,1412200878929),
	(76,1412200890286),
	(77,1412200890297),
	(78,1412200962283),
	(79,1412201088779),
	(80,1412201254375),
	(81,1412662889439),
	(82,1412663011397),
	(83,1412663070332),
	(84,1412663287384),
	(85,1412663287636),
	(86,1412663287783),
	(87,1412663287817);

/*!40000 ALTER TABLE `REVINFO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TASK
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TASK`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
