/*
Navicat MySQL Data Transfer

Source Server         : 192.168.22.52(root)
Source Server Version : 50516
Source Host           : 192.168.22.52:3306
Source Database       : odpdb

Target Server Type    : MYSQL
Target Server Version : 50516
File Encoding         : 65001

Date: 2013-09-03 15:49:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `file_upload_table`
-- ----------------------------
DROP TABLE IF EXISTS `file_upload_table`;
CREATE TABLE `file_upload_table` (
  `file_id` varchar(255) NOT NULL,
  `load_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `flag` int(11) NOT NULL,
  `cnt` bigint(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
