use viz45;

DROP TABLE IF EXISTS `dbconnection`;
CREATE TABLE `dbconnection` (
  `connId` int(11) NOT NULL AUTO_INCREMENT,
  `connName` varchar(50) DEFAULT NULL,
  `connType` varchar(50) DEFAULT NULL,
  `connDriver` varchar(50) DEFAULT NULL,
  `connUrl` varchar(500) DEFAULT NULL,
  `connUser` varchar(50) DEFAULT NULL,
  `connPassword` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`connId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `execution`;
CREATE TABLE `execution` (
  `exeId` varchar(50) NOT NULL DEFAULT '',
  `taskId` varchar(50) DEFAULT NULL,
  `executionType` varchar(50) DEFAULT NULL,
  `options` text,
  `startAt` datetime DEFAULT NULL,
  `endAt` datetime DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `sqlId` int(11) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `recordNumber` bigint(11) NOT NULL DEFAULT '0',
  `exportState` int(11) NOT NULL DEFAULT '3',
  `code` text,
  PRIMARY KEY (`exeId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `resourceId` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(100) DEFAULT NULL,
  `resourceType` varchar(50) DEFAULT NULL,
  `resourceName` varchar(500) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `fullPath` varchar(500) DEFAULT NULL COMMENT 'id.id.id',
  PRIMARY KEY (`resourceId`)
) ENGINE=MyISAM AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `script`;
CREATE TABLE `script` (
  `resourceId` int(11) DEFAULT NULL,
  `script` text
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sqlinfo`;
CREATE TABLE `sqlinfo` (
  `sqlId` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(100) DEFAULT NULL,
  `sqlStmt` text,
  `exportPrivilege` bit(1) NOT NULL DEFAULT b'0',
  `validStartDate` datetime DEFAULT NULL,
  `validEndDate` datetime DEFAULT NULL,
  `exportReason` varchar(4096) DEFAULT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sqlId`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `taskId` varchar(50) DEFAULT NULL,
  `taskName` varchar(50) DEFAULT NULL,
  `taskType` varchar(50) DEFAULT NULL,
  `options` varchar(5000) DEFAULT NULL COMMENT 'json',
  `owner` varchar(50) DEFAULT NULL,
  `lastModTime` datetime DEFAULT NULL,
  `isTempTask` tinyint(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tasktype`;
CREATE TABLE `tasktype` (
  `taskType` varchar(50) DEFAULT NULL,
  `possibleOptions` varchar(5000) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user` varchar(100) NOT NULL DEFAULT '',
  `pwd` varchar(255) DEFAULT NULL,
  `hivedbs` varchar(100) DEFAULT NULL,
  `privilege` varchar(50) DEFAULT NULL,
  `maxRunningSqls` int(11) NOT NULL DEFAULT '5',
  `connId` int(11) DEFAULT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
