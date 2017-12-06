/*
Navicat MySQL Data Transfer

Source Server         : 192.168.22.52(root)
Source Server Version : 50516
Source Host           : 192.168.22.52:3306
Source Database       : odpdb

Target Server Type    : MYSQL
Target Server Version : 50516
File Encoding         : 65001

Date: 2013-09-03 15:50:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user_table`
-- ----------------------------
DROP TABLE IF EXISTS `user_table`;
CREATE TABLE `user_table` (
  `app_id` varchar(255) NOT NULL DEFAULT '',
  `access_code` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_table
-- ----------------------------
INSERT INTO `user_table` VALUES ('1', '1000000000000PLHDEFQCHMFVPUHX0001', '1');
INSERT INTO `user_table` VALUES ('2', 'E000000000000PLHDEFQCHMFVPUHX0002', '2');
