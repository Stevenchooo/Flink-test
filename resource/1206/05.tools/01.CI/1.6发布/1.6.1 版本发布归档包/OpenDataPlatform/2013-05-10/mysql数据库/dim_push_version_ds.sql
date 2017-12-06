/*
Navicat MySQL Data Transfer

Source Server         : 192.168.8.246(root)
Source Server Version : 50516
Source Host           : 192.168.8.246:3306
Source Database       : corpidmdb

Target Server Type    : MYSQL
Target Server Version : 50516
File Encoding         : 65001

Date: 2013-04-30 15:31:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dim_push_version_ds`
-- ----------------------------
DROP TABLE IF EXISTS `dim_push_version_ds`;
CREATE TABLE `dim_push_version_ds` (
  `push_version` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

