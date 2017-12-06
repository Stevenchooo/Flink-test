-- MySQL dump 10.13  Distrib 5.5.16, for Linux (x86_64)
--
-- Host: localhost    Database: usermanager
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
-- Table structure for table `dbserver_config`
--

DROP TABLE IF EXISTS `dbserver_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dbserver_config` (
  `tcc_name` varchar(255) NOT NULL,
  `tcc_url` varchar(512) NOT NULL,
  `ip_address` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `db_name` varchar(255) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  PRIMARY KEY (`tcc_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `node_cluster`
--

DROP TABLE IF EXISTS `node_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node_cluster` (
  `cluster_id` int(11) NOT NULL DEFAULT '0',
  `node_type` int(11) DEFAULT NULL,
  `node_list` int(11) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cluster_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `node_info`
--

DROP TABLE IF EXISTS `node_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node_info` (
  `node_id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `host` varchar(255) NOT NULL DEFAULT '127.0.0.1',
  `port` int(11) NOT NULL DEFAULT '22',
  `rootPwd` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  UNIQUE KEY `unique` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operate_audit_info`
--

DROP TABLE IF EXISTS `operate_audit_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operate_audit_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(255) NOT NULL,
  `operator_time` datetime NOT NULL,
  `op_type` int(11) NOT NULL,
  `service_id` longtext,
  `task_id` longtext,
  `oper_parameters` longtext,
  `login_ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator_info`
--

DROP TABLE IF EXISTS `operator_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_info` (
  `operator_name` varchar(255) NOT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `role_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`operator_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_user`
--

DROP TABLE IF EXISTS `os_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_user` (
  `os_user` varchar(255) NOT NULL,
  `pem_key` varchar(8192) DEFAULT NULL,
  `user_group` varchar(255) NOT NULL,
  `desc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`os_user`),
  KEY `os_user_foreign_group` (`user_group`),
  CONSTRAINT `os_user_foreign_group` FOREIGN KEY (`user_group`) REFERENCES `user_group` (`user_group`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_config`
--

DROP TABLE IF EXISTS `report_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_config` (
  `node_id` int(11) NOT NULL DEFAULT '0',
  `browser_port` int(11) DEFAULT NULL,
  `cpt_dir` varchar(1000) DEFAULT NULL,
  `address_prefix` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_info`
--

DROP TABLE IF EXISTS `role_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_info` (
  `role_id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `os_users` varchar(4092) DEFAULT NULL,
  `other_groups` varchar(4092) DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name_unique` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_defination`
--

DROP TABLE IF EXISTS `service_defination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_defination` (
  `service_id` int(11) NOT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `desc` varchar(2048) DEFAULT NULL,
  `contact_person` varchar(255) DEFAULT NULL,
  `alarm_email_list` varchar(512) DEFAULT NULL,
  `alarm_mobile_list` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_name` (`service_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `user_group` varchar(100) NOT NULL DEFAULT '',
  `service_id` int(11) DEFAULT NULL,
  `desc` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`user_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `user_name` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `master_group_name` varchar(50) DEFAULT NULL,
  `is_data_in_dir` tinyint(50) DEFAULT NULL,
  `mfs_data_in_dir` varchar(50) DEFAULT NULL,
  `is_data_out_dir` tinyint(11) DEFAULT NULL,
  `mfs_data_out_dir` varchar(500) DEFAULT NULL,
  `is_share_dir` tinyint(4) DEFAULT NULL,
  `mfs_share_dir` varchar(255) DEFAULT NULL,
  `is_hdfs_dir` tinyint(4) DEFAULT NULL,
  `hdfs_dir` varchar(255) DEFAULT NULL,
  `is_hadoop_queue` tinyint(4) DEFAULT NULL,
  `hadoop_queue_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_profile` (
  `user_name` varchar(50) NOT NULL DEFAULT '',
  `cpt_location` varchar(500) NOT NULL DEFAULT '',
  `cpt_default_load_node` int(11) NOT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'usermanager'
--

--
-- Dumping routines for database 'usermanager'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-27 13:41:12