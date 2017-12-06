use tcc2rs;

DROP TABLE IF EXISTS `alarm_fact_info`;
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

DROP TABLE IF EXISTS `node_info`;
CREATE TABLE `node_info` (
  `node_id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `host` varchar(255) NOT NULL DEFAULT '127.0.0.1',
  `port` int(11) NOT NULL DEFAULT '22',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  UNIQUE KEY `unique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `operate_audit_info`;
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
) ENGINE=InnoDB AUTO_INCREMENT=13467 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `operator_info`;
CREATE TABLE `operator_info` (
  `operator_name` varchar(255) NOT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `role_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`operator_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `os_user`;
CREATE TABLE `os_user` (
  `os_user` varchar(255) NOT NULL,
  `pem_key` varchar(8192) DEFAULT NULL,
  `user_group` varchar(255) NOT NULL,
  `desc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`os_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `role_info`;
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


DROP TABLE IF EXISTS `service_defination`;
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

DROP TABLE IF EXISTS `task_alarm_channel_info`;
CREATE TABLE `task_alarm_channel_info` (
  `task_id` int(11) NOT NULL,
  `alarm_grade` int(11) NOT NULL,
  `email_list` varchar(512) DEFAULT NULL,
  `mobile_list` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`task_id`,`alarm_grade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `task_alarm_items`;
CREATE TABLE `task_alarm_items` (
  `task_id` bigint(11) NOT NULL,
  `alarm_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `is_alarm_permitted` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `task_alarm_threshold`;
CREATE TABLE `task_alarm_threshold` (
  `task_id` int(11) NOT NULL,
  `latest_start_time` varchar(255) DEFAULT NULL,
  `latest_end_time` varchar(255) DEFAULT NULL,
  `max_run_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tcc_batch_running_state`;
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


DROP TABLE IF EXISTS `tcc_remoteshell_log`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tcc_step_running_state`;
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


DROP TABLE IF EXISTS `tcc_stepid_serial`;
CREATE TABLE `tcc_stepid_serial` (
  `task_Id` bigint(20) NOT NULL,
  `serialNo` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`task_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tcc_task`;
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
  `End_Batch_Flag` tinyint(4) DEFAULT '0',
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
  `creator` varchar(255) NOT NULL,
  `os_user` varchar(255) NOT NULL,
  `deployed_node_list` varchar(255) NOT NULL DEFAULT '1',
  `user_group` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`Task_ID`),
  UNIQUE KEY `Task_Name` (`Task_Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tcc_task_running_state`;
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
  `node_name` varchar(255) DEFAULT NULL,
  `node_sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`Task_ID`,`Cycle_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tcc_task_step`;
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
  PRIMARY KEY (`Task_ID`,`Step_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tcc_taskcontrolcenter_log`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tcc_taskid_serial`;
CREATE TABLE `tcc_taskid_serial` (
  `serviceID` int(2) NOT NULL,
  `taskType` int(2) NOT NULL,
  `serialNo` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`serviceID`,`taskType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `user_group` varchar(100) NOT NULL DEFAULT '',
  `service_id` int(11) NOT NULL,
  `desc` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`user_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `tcc_task` ADD CONSTRAINT `task_foreign_os_user` FOREIGN KEY (`os_user`) REFERENCES `os_user` (`os_user`) ON DELETE RESTRICT ON UPDATE NO ACTION;
ALTER TABLE `os_user` ADD CONSTRAINT `os_user_foreign_group` FOREIGN KEY (`user_group`) REFERENCES `user_group` (`user_group`) ON DELETE RESTRICT ON UPDATE NO ACTION;
ALTER TABLE `user_group` ADD CONSTRAINT `group_foreign_service_id` FOREIGN KEY (`service_id`) REFERENCES `service_defination` (`service_id`) ON DELETE RESTRICT ON UPDATE NO ACTION;
