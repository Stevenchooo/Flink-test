/*
Navicat MySQL Data Transfer

Source Server         : tongji-db
Source Server Version : 50620
Source Host           : 192.168.6.139:3306
Source Database       : manager_test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-01-27 22:39:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_wda_province_code`
-- ----------------------------
DROP TABLE IF EXISTS `t_wda_province_code`;
CREATE TABLE `t_wda_province_code` (
  `code` varchar(10) NOT NULL DEFAULT '0',
  `province_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wda_province_code
-- ----------------------------
INSERT INTO `t_wda_province_code` VALUES ('TIB', '西藏');
INSERT INTO `t_wda_province_code` VALUES ('BEJ', '北京市');
INSERT INTO `t_wda_province_code` VALUES ('GUI', '贵州省');
INSERT INTO `t_wda_province_code` VALUES ('FUJ', '福建省');
INSERT INTO `t_wda_province_code` VALUES ('CHQ', '重庆市');
INSERT INTO `t_wda_province_code` VALUES ('SCH', '四川省');
INSERT INTO `t_wda_province_code` VALUES ('SHH', '上海市');
INSERT INTO `t_wda_province_code` VALUES ('JSU', '江苏省');
INSERT INTO `t_wda_province_code` VALUES ('ZHJ', '浙江省');
INSERT INTO `t_wda_province_code` VALUES ('SHX', '山西省');
INSERT INTO `t_wda_province_code` VALUES ('ZHJ', '内蒙古');
INSERT INTO `t_wda_province_code` VALUES ('TAJ', '天津市');
INSERT INTO `t_wda_province_code` VALUES ('HEB', '河北省');
INSERT INTO `t_wda_province_code` VALUES ('ANH', '安徽省');
INSERT INTO `t_wda_province_code` VALUES ('JXI', '江西省');
INSERT INTO `t_wda_province_code` VALUES ('SHD', '山东省');
INSERT INTO `t_wda_province_code` VALUES ('HEN', '河南省');
INSERT INTO `t_wda_province_code` VALUES ('HUN', '湖南省');
INSERT INTO `t_wda_province_code` VALUES ('HUB', '湖北省');
INSERT INTO `t_wda_province_code` VALUES ('GXI', '广西');
INSERT INTO `t_wda_province_code` VALUES ('GUD', '广东省');
INSERT INTO `t_wda_province_code` VALUES ('HAI', '海南省');
INSERT INTO `t_wda_province_code` VALUES ('XIN', '新疆');
INSERT INTO `t_wda_province_code` VALUES ('NXA', '宁夏');
INSERT INTO `t_wda_province_code` VALUES ('QIH', '青海省');
INSERT INTO `t_wda_province_code` VALUES ('GAN', '甘肃省');
INSERT INTO `t_wda_province_code` VALUES ('SHA', '陕西省');
INSERT INTO `t_wda_province_code` VALUES ('HLJ', '黑龙江省');
INSERT INTO `t_wda_province_code` VALUES ('JIL', '吉林省');
INSERT INTO `t_wda_province_code` VALUES ('CN-53', '云南省');
INSERT INTO `t_wda_province_code` VALUES ('LIA', '辽宁省');
