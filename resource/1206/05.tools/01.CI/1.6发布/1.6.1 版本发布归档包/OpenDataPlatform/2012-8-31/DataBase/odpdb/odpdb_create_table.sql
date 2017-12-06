-- MySQL dump 10.13  Distrib 5.5.16, for Linux (x86_64)
--
-- Host: 192.168.8.246    Database: odpdb
-- ------------------------------------------------------
-- Server version	5.5.16-enterprise-commercial-advanced-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_info_table`
--

DROP TABLE IF EXISTS `audit_info_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_info_table` (
  `reserve_id` bigint(20) NOT NULL,
  `operator_type` int(4) NOT NULL,
  `app_id` varchar(255) NOT NULL,
  `operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `record_number` bigint(20) NOT NULL DEFAULT '0',
  `app_transaction_key` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`reserve_id`,`operator_type`,`app_id`,`operator_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config_table`
--

DROP TABLE IF EXISTS `config_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_table` (
  `pushSvrAddr` varchar(2048) NOT NULL,
  `hiADAddr` varchar(2048) NOT NULL,
  `loadCtrlThresHold` int(11) NOT NULL,
  `tokenExpiredTime` int(11) NOT NULL,
  `maxReservedDays` int(11) NOT NULL DEFAULT '31',
  `maxReturnRecords` int(11) DEFAULT NULL,
  `routineExecuteTime` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `control_flag_table`
--

DROP TABLE IF EXISTS `control_flag_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `control_flag_table` (
  `currentTableID` int(11) NOT NULL,
  `currentRouteTable` int(11) NOT NULL,
  `loadCtrlValue` int(11) NOT NULL,
  `currentServerID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dbserver_address_table`
--

DROP TABLE IF EXISTS `dbserver_address_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dbserver_address_table` (
  `db_server_id` int(11) NOT NULL,
  `ip_address` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `DBName` varchar(255) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  PRIMARY KEY (`db_server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dialog_token_table`
--

DROP TABLE IF EXISTS `dialog_token_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dialog_token_table` (
  `Token` varchar(255) NOT NULL,
  `expiredTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reserved_info_table`
--

DROP TABLE IF EXISTS `reserved_info_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reserved_info_table` (
  `reserve_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `expired_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tmp_table_name` varchar(255) DEFAULT NULL,
  `column_name_list` varchar(4096) DEFAULT NULL,
  `state` int(11) NOT NULL DEFAULT '0',
  `file_url` varchar(4096) DEFAULT NULL,
  `create_app_id` varchar(255) NOT NULL,
  PRIMARY KEY (`reserve_id`)
) ENGINE=InnoDB AUTO_INCREMENT=619 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `route_table_1`
--

DROP TABLE IF EXISTS `route_table_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_table_1` (
  `table_name` varchar(255) NOT NULL DEFAULT '',
  `db_server_id` int(11) NOT NULL DEFAULT '0',
  `Hash_value` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`table_name`,`db_server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `route_table_2`
--

DROP TABLE IF EXISTS `route_table_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_table_2` (
  `table_name` varchar(255) NOT NULL DEFAULT '',
  `db_server_id` int(11) NOT NULL DEFAULT '0',
  `Hash_value` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`table_name`,`db_server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_table`
--

DROP TABLE IF EXISTS `user_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_table` (
  `app_id` varchar(255) NOT NULL DEFAULT '',
  `access_code` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-23  6:13:24
