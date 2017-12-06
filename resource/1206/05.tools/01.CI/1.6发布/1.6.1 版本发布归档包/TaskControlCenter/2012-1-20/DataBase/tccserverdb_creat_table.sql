SET FOREIGN_KEY_CHECKS=0;

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


DROP TABLE IF EXISTS `tcc_task`;
CREATE TABLE `tcc_task` (
  `Task_ID` bigint(20) NOT NULL,
  `ServiceID` tinyint(4) NOT NULL,
  `Task_Name` varchar(64) NOT NULL,
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
  `End_Batch_Flag` tinyint(4) NOT NULL,
  `Input_File_list` varchar(2048) DEFAULT NULL,
  `Input_File_Min_Count` int(4) DEFAULT NULL,
  `Wait_Input_Minutes` int(11) NOT NULL,
  `Create_Time` datetime NOT NULL,
  `Last_Update_Time` datetime DEFAULT NULL,
  `Files_in_host` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Task_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tcc_task_running_state`;
CREATE TABLE `tcc_task_running_state` (
  `Task_ID` bigint(20) NOT NULL,
  `Cycle_ID` varchar(16) NOT NULL,
  `State` smallint(4) NOT NULL,
  `Running_Start_Time` datetime DEFAULT NULL,
  `Running_End_Time` datetime DEFAULT NULL,
  `Begin_Depend_Task_List` text DEFAULT '',
  `Finish_Depend_Task_List` text DEFAULT '',
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
  `Timeout_Minutes` tinyint(3) NOT NULL,
  `Command` mediumtext NOT NULL,
  `Create_Time` datetime NOT NULL,
  `Last_Update_Time` datetime DEFAULT NULL,
  `Exec_cmd_host` varchar(15) NOT NULL,
  PRIMARY KEY (`Task_ID`,`Step_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
