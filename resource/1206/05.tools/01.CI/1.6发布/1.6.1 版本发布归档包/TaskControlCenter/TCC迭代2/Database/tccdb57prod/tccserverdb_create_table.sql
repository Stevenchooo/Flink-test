SET FOREIGN_KEY_CHECKS=0;

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
-- Table structure for table `alarm_fact_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alarm_fact_info` (
  `task_id` int(11) NOT NULL,
  `cycle_id` varchar(50) NOT NULL,
  `alarm_time` datetime NOT NULL,
  `service_id` int(255) DEFAULT NULL,
  `instance_id` int(11) DEFAULT NULL,
  `alarm_grade` int(11) DEFAULT NULL,
  `alarm_type` int(11) NOT NULL DEFAULT '0',
  `email_list` varchar(512) DEFAULT NULL,
  `mobile_list` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `reason` varchar(1024) DEFAULT NULL,
  `solution` varchar(1024) DEFAULT NULL,
  `operator_name` varchar(255) DEFAULT NULL,
  `process_time` datetime DEFAULT NULL,
  PRIMARY KEY (`task_id`,`cycle_id`,`alarm_time`,`alarm_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `node_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node_info` (
  `node_ip_addr` varchar(255) NOT NULL,
  `node_type` int(11) NOT NULL,
  `CPU` varchar(255) DEFAULT NULL,
  `memory` bigint(20) DEFAULT NULL,
  `storage` bigint(20) DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`node_ip_addr`,`node_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operate_audit_info`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=1154 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_info` (
  `operator_name` varchar(255) NOT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `os_user_name` varchar(255) NOT NULL,
  `role_id` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`operator_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_user_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_user_info` (
  `user_name` varchar(255) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `desc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_defination`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_defination` (
  `role_id` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_privilege_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_privilege_info` (
  `role_id` varchar(255) NOT NULL,
  `service_id` int(11) NOT NULL,
  `task_group` varchar(255) NOT NULL,
  `privilege_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`service_id`,`task_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_defination`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_defination` (
  `service_id` int(11) NOT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `desc` varchar(2048) DEFAULT NULL,
  `default_priority` int(11) DEFAULT NULL,
  `max_reduce_resource` int(11) DEFAULT NULL,
  `contact_person` varchar(255) DEFAULT NULL,
  `alarm_email_list` varchar(512) DEFAULT NULL,
  `alarm_mobile_list` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_name` (`service_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_deploy_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_deploy_info` (
  `service_id` int(11) NOT NULL,
  `node_type` int(11) NOT NULL,
  `node_ip_addr` varchar(255) DEFAULT NULL,
  `data_directory` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`service_id`,`node_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_task_group_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_task_group_info` (
  `service_id` int(11) NOT NULL,
  `task_group` varchar(255) NOT NULL,
  `desc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`service_id`,`task_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_alarm_channel_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_alarm_channel_info` (
  `task_id` int(11) NOT NULL,
  `alarm_grade` int(11) NOT NULL,
  `email_list` varchar(512) DEFAULT NULL,
  `mobile_list` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`task_id`,`alarm_grade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_alarm_items`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_alarm_items` (
  `task_id` bigint(11) NOT NULL,
  `alarm_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `is_alarm_permitted` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_alarm_threshold`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_alarm_threshold` (
  `task_id` int(11) NOT NULL,
  `latest_start_time` varchar(255) DEFAULT NULL,
  `latest_end_time` varchar(255) DEFAULT NULL,
  `max_run_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_batch_running_state`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_batch_running_state` (
  `Task_ID` bigint(20) NOT NULL,
  `Cycle_ID` varchar(16) NOT NULL,
  `Batch_ID` int(9) NOT NULL,
  `State` smallint(4) NOT NULL,
  `Running_Start_Time` datetime DEFAULT NULL,
  `Running_End_Time` datetime DEFAULT NULL,
  `Job_Input` varchar(2048) NOT NULL,
  PRIMARY KEY (`Task_ID`,`Cycle_ID`,`Batch_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_remoteshell_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_remoteshell_log` (
  `logId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) DEFAULT NULL,
  `taskId` varchar(20) DEFAULT NULL,
  `cycleId` varchar(50) DEFAULT NULL,
  `createTime` varchar(50) DEFAULT NULL,
  `threadName` varchar(500) DEFAULT NULL,
  `className` varchar(500) DEFAULT NULL,
  `methodName` varchar(100) DEFAULT NULL,
  `logLevel` varchar(50) DEFAULT NULL,
  `message` longtext,
  `clientIp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`logId`),
  KEY `userName` (`userName`),
  KEY `taskId` (`taskId`),
  KEY `cycleId` (`cycleId`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=4313110 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_step_running_state`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_step_running_state` (
  `Task_ID` bigint(20) NOT NULL,
  `Cycle_ID` varchar(16) NOT NULL,
  `Batch_ID` int(9) NOT NULL,
  `Step_ID` int(4) NOT NULL,
  `State` smallint(4) NOT NULL,
  `Running_Start_Time` datetime DEFAULT NULL,
  `Running_End_Time` datetime DEFAULT NULL,
  `Running_Job_ID` varchar(2048) DEFAULT NULL,
  `Retry_Count` smallint(4) DEFAULT NULL,
  `Job_Input_List` mediumtext,
  `Job_Output_List` mediumtext,
  `Fail_Reason` mediumtext,
  PRIMARY KEY (`Task_ID`,`Cycle_ID`,`Batch_ID`,`Step_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_stepid_serial`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_stepid_serial` (
  `task_Id` bigint(20) NOT NULL,
  `serialNo` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`task_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_task`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_task` (
  `Task_ID` bigint(20) NOT NULL,
  `serviceid` int(11) NOT NULL,
  `Task_Name` varchar(128) NOT NULL,
  `Task_Desc` varchar(512) NOT NULL,
  `Task_Type` tinyint(2) NOT NULL,
  `Task_Enable_Flag` tinyint(1) NOT NULL DEFAULT '0',
  `Task_State` tinyint(1) NOT NULL DEFAULT '1',
  `Priority` int(4) NOT NULL DEFAULT '5',
  `Cycle_Type` varchar(1) NOT NULL,
  `Cycle_Length` int(4) NOT NULL,
  `Cycle_Offset` varchar(50) NOT NULL,
  `Depend_Task_ID_List` text,
  `Cycle_Depend_Flag` tinyint(1) NOT NULL,
  `Multi_Batch_Flag` tinyint(1) NOT NULL,
  `End_Batch_Flag` tinyint(4) NOT NULL DEFAULT '0',
  `Input_File_list` varchar(2048) DEFAULT NULL,
  `Input_File_Min_Count` int(4) DEFAULT NULL,
  `Wait_Input_Minutes` int(11) NOT NULL,
  `Create_Time` datetime NOT NULL,
  `Last_Update_Time` datetime DEFAULT NULL,
  `Files_in_host` varchar(15) DEFAULT NULL,
  `redo_Start_Time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `redo_End_Time` datetime NOT NULL DEFAULT '2020-01-01 00:00:00',
  `redo_Day_Length` int(11) NOT NULL DEFAULT '0',
  `start_Time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `weight` int(11) NOT NULL DEFAULT '1',
  `Redo_Type` int(11) NOT NULL DEFAULT '1',
  `Start_Operator` varchar(255) DEFAULT NULL,
  `OS_User_Name` varchar(255) DEFAULT NULL,
  `service_task_group` varchar(255) NOT NULL DEFAULT '默认',
  PRIMARY KEY (`Task_ID`),
  UNIQUE KEY `Task_Name` (`Task_Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_task_running_state`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_task_running_state` (
  `Task_ID` bigint(20) NOT NULL,
  `Cycle_ID` varchar(16) NOT NULL,
  `State` smallint(4) NOT NULL,
  `Running_Start_Time` datetime DEFAULT NULL,
  `Running_End_Time` datetime DEFAULT NULL,
  `Begin_Depend_Task_List` text,
  `Finish_Depend_Task_List` text,
  `Has_Alarm_Latest_Start` tinyint(4) NOT NULL DEFAULT '0',
  `Return_Times` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Task_ID`,`Cycle_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_task_step`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_task_step` (
  `Task_ID` bigint(20) NOT NULL,
  `Step_ID` int(4) NOT NULL,
  `Step_Name` varchar(256) NOT NULL,
  `Step_Desc` varchar(1024) NOT NULL,
  `Step_Task_Type` tinyint(2) NOT NULL,
  `Step_Enable_Flag` tinyint(1) NOT NULL,
  `Allow_Retry_Count` int(10) NOT NULL DEFAULT '3',
  `Timeout_Minutes` int(10) NOT NULL,
  `Command` mediumtext NOT NULL,
  `Create_Time` datetime NOT NULL,
  `Last_Update_Time` datetime DEFAULT NULL,
  `Exec_cmd_host` varchar(15) NOT NULL,
  PRIMARY KEY (`Task_ID`,`Step_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_taskcontrolcenter_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_taskcontrolcenter_log` (
  `logId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) DEFAULT NULL,
  `taskId` varchar(20) DEFAULT NULL,
  `cycleId` varchar(50) DEFAULT NULL,
  `createTime` varchar(50) DEFAULT NULL,
  `threadName` varchar(500) DEFAULT NULL,
  `className` varchar(500) DEFAULT NULL,
  `methodName` varchar(100) DEFAULT NULL,
  `logLevel` varchar(50) DEFAULT NULL,
  `message` longtext,
  `clientIp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`logId`),
  KEY `userName` (`userName`),
  KEY `taskId` (`taskId`),
  KEY `cycleId` (`cycleId`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=277040 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tcc_taskid_serial`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcc_taskid_serial` (
  `serviceID` int(2) NOT NULL,
  `taskType` int(2) NOT NULL,
  `serialNo` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`serviceID`,`taskType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;