/*
Navicat MySQL Data Transfer

Source Server         : tongji-db
Source Server Version : 50620
Source Host           : 192.168.6.139:3306
Source Database       : manager_test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-01-27 22:39:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_wda_group`
-- ----------------------------
DROP TABLE IF EXISTS `t_wda_group`;
CREATE TABLE `t_wda_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `members` varchar(400) DEFAULT NULL,
  `membersName` varchar(400) DEFAULT '',
  `creator` varchar(20) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wda_group
-- ----------------------------

