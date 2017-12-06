/*
Navicat MySQL Data Transfer

Source Server         : 192.168.22.52(root)
Source Server Version : 50516
Source Host           : 192.168.22.52:3306
Source Database       : odpdb

Target Server Type    : MYSQL
Target Server Version : 50516
File Encoding         : 65001

Date: 2013-09-03 15:49:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `reserved_new_info_table`
-- ----------------------------
DROP TABLE IF EXISTS `reserved_new_info_table`;
CREATE TABLE `reserved_new_info_table` (
  `group_id` varchar(255) NOT NULL,
  `use_date` datetime NOT NULL,
  `expired_date` datetime NOT NULL,
  `days` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `cnt` bigint(20) DEFAULT NULL,
  `filter_stmt` varchar(8192) NOT NULL,
  `imei_file_id` varchar(256) DEFAULT NULL,
  `channels` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
