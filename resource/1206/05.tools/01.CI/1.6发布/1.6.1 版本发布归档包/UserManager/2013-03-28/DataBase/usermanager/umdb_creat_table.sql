use usermanager;

DROP TABLE IF EXISTS `dbserver_config`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `user_group` varchar(100) NOT NULL DEFAULT '',
  `service_id` int(11) DEFAULT NULL,
  `desc` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`user_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `node_or_cluster_id` int(11) NOT NULL,
  `app_type` int(11) NOT NULL,
  `user` varchar(50) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `owner_group_name` varchar(50) DEFAULT NULL,
  `home_dir` varchar(50) DEFAULT NULL,
  `group_list` varchar(50) DEFAULT NULL,
  `db_id` int(11) DEFAULT NULL,
  `desc` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`node_or_cluster_id`,`app_type`,`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `os_user` ADD CONSTRAINT `os_user_foreign_group` FOREIGN KEY (`user_group`) REFERENCES `user_group` (`user_group`) ON DELETE RESTRICT ON UPDATE NO ACTION;
