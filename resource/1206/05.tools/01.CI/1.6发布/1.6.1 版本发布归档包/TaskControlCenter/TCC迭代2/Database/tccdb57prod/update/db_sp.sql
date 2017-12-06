alter table `tcc_task`
	add `Start_Operator` varchar(255) DEFAULT NULL,
	add `OS_User_Name` varchar(255) DEFAULT NULL,
  add `service_task_group` varchar(255) NOT NULL DEFAULT '默认',
  drop `Success_SendEmail_Flag`,
  drop `Failure_EmailsTo`;
  
alter table `tcc_task_running_state`
  add `Has_Alarm_Latest_Start` tinyint(4) NOT NULL DEFAULT '0',
  add `Return_Times` int(11) NOT NULL DEFAULT '0';
  
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
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
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


INSERT INTO `role_defination` (role_id,create_time,`desc`) VALUES ('系统管理员',now(),'系统管理员');
INSERT INTO `os_user_info` (user_name,pwd,`desc`) VALUES ('hadoop-NJ','','hadoop-NJ');
INSERT INTO `operator_info` (operator_name,pwd,`desc`,os_user_name,role_id,create_time) VALUES ('system','system','系统管理员','hadoop-NJ','系统管理员',now());

INSERT INTO `node_info` VALUES ('10.120.5.53',1,'8',1024,500,'数据通道'),('10.120.5.57',2,'8',1024,500,'hadoop网关');

INSERT INTO `service_defination` VALUES (0,'Dbank','Dbank',5,100,'Dbank','','','2012-06-19 11:21:04'),(1,'终端云','终端云',5,100,'终端云','','','2012-06-19 11:21:21'),(2,'天天浏览器','天天浏览器',5,100,'天天浏览器','','','2012-06-19 11:21:30'),(3,'Hotalk','Hotalk',5,100,'Hotalk','','','2012-06-19 11:21:40'),(4,'智汇云','智汇云',5,100,'智汇云','','','2012-06-19 11:21:53'),(5,'天天家园','天天家园(地址本,空间,天天秀)',5,100,'天天家园','','','2012-06-19 11:22:42'),(6,'天天记事','天天记事',5,100,'天天记事','','','2012-06-19 11:22:56'),(7,'企业通讯','UC2(企业通讯)',5,100,'企业通讯','','','2012-06-19 11:23:24'),(8,'云服务','HWS(云服务)',5,100,'云服务','','','2012-06-19 11:23:48'),(9,'虚拟主机','虚拟主机',5,100,'虚拟主机','','','2012-06-19 11:23:56'),(10,'云桌面','云桌面',5,100,'','','','2012-06-19 11:24:07'),(11,'S3虚拟存储','S3虚拟存储',5,100,'','','','2012-06-19 11:24:17'),(12,'WEBOS','WEBOS',5,100,'','','','2012-06-19 11:24:25'),(13,'VoIP','VoIP',5,100,'','','','2012-06-19 11:24:39'),(14,'天天微讯','天天微讯',5,100,'','','','2012-06-19 11:24:47'),(15,'网盘','网盘',5,100,'','','','2012-06-19 11:24:55'),(16,'相册','相册',5,100,'','','','2012-06-19 11:25:02'),(17,'Phone+','Phone+',5,100,'','','','2012-06-19 11:28:44'),(18,'Push','Push',5,100,'','','','2012-06-19 11:29:05'),(19,'游戏平台','游戏平台',5,100,'','','','2012-06-19 11:29:15'),(20,'手机支付','手机支付',5,100,'','','','2012-06-19 11:29:24'),(21,'SNS','SNS',5,100,'','','','2012-06-19 11:29:34'),(22,'emotion论坛','emotion论坛',6,100,'','','','2012-06-19 17:07:33'),(90,'智汇云开发者','智汇云开发者',5,100,'','','','2012-06-19 11:30:04'),(999,'全业务','全业务',5,100,'','','','2012-06-19 11:30:13');
INSERT INTO `service_task_group_info` SELECT `service_id`,'默认','默认' FROM `service_defination`;

INSERT INTO `task_alarm_items` (task_id,alarm_type) SELECT `task_id`,'0101110011' FROM `tcc_task`;

UPDATE `tcc_task` SET `Start_Operator`='system',`OS_User_Name`='hadoop-NJ';