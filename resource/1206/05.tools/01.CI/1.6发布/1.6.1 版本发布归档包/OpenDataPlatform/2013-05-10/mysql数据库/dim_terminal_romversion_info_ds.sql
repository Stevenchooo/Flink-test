/*
Navicat MySQL Data Transfer

Source Server         : 192.168.8.246(root)
Source Server Version : 50516
Source Host           : 192.168.8.246:3306
Source Database       : corpidmdb

Target Server Type    : MYSQL
Target Server Version : 50516
File Encoding         : 65001

Date: 2013-04-30 15:30:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dim_terminal_rom_info_ds`
-- ----------------------------
DROP TABLE IF EXISTS `dim_terminal_romversion_info_ds`;
CREATE TABLE `dim_terminal_romversion_info_ds` (
  `terminal_type` varchar(255) DEFAULT NULL,
  `rom_version` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
