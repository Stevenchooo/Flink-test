/*
Navicat MySQL Data Transfer

Source Server         : tongji-db
Source Server Version : 50620
Source Host           : 192.168.6.139:3306
Source Database       : manager_test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-01-27 22:39:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_wda_group_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_wda_group_user`;
CREATE TABLE `t_wda_group_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupId` int(20) DEFAULT NULL,
  `userId` varchar(50) DEFAULT NULL,
  `userName` varchar(50) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wda_group_user
-- ----------------------------

