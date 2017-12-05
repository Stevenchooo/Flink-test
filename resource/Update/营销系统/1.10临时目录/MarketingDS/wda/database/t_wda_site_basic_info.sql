/*
Navicat MySQL Data Transfer

Source Server         : tongji-db
Source Server Version : 50620
Source Host           : 192.168.6.139:3306
Source Database       : manager_test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-01-27 22:39:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_wda_site_basic_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_wda_site_basic_info`;
CREATE TABLE `t_wda_site_basic_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` varchar(64) NOT NULL,
  `site_name` varchar(50) DEFAULT NULL,
  `site_url` varchar(200) DEFAULT NULL,
  `site_desc` varchar(100) DEFAULT NULL,
  `site_type` tinyint(1) DEFAULT '0',
  `access_net_type` tinyint(255) DEFAULT '0',
  `is_default` tinyint(255) DEFAULT '0',
  `business_category` tinyint(255) DEFAULT '0',
  `creator` varchar(50) DEFAULT '',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `group_id` int(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wda_site_basic_info
-- ----------------------------
INSERT INTO `t_wda_site_basic_info` VALUES ('31', 'www.vmall.com', '华为商城-pc端', 'http://www.vmall.com', null, '0', '0', '0', '0', 'admin', '2016-01-10 16:22:55', '43');
INSERT INTO `t_wda_site_basic_info` VALUES ('32', 'm.vmall.com', '华为商城-移动端1', 'http://m.vmall.com', null, '0', '0', '0', '0', 'admin', '2016-01-10 16:23:25', '43');
INSERT INTO `t_wda_site_basic_info` VALUES ('33', 'cn.club.vmall.com', '花粉论坛', 'http://cn.club.vmall.com/', null, '0', '0', '0', '0', 'admin', '2016-01-10 16:24:56', '44');
INSERT INTO `t_wda_site_basic_info` VALUES ('34', 'consumer.huawei.com', '华为消费者业务官网', 'http://consumer.huawei.com/cn/', null, '0', '0', '0', '0', 'admin', '2016-01-10 16:25:31', '45');
INSERT INTO `t_wda_site_basic_info` VALUES ('35', 'm.honor.cn', '华为荣耀官网-移动端', 'http://m.honor.cn', null, '0', '0', '0', '0', 'admin', '2016-01-10 16:26:45', null);
INSERT INTO `t_wda_site_basic_info` VALUES ('36', 'www.honor.cn', '华为荣耀官网-PC端', 'http://www.honor.cn', null, '0', '0', '0', '0', 'admin', '2016-01-10 16:27:50', null);
INSERT INTO `t_wda_site_basic_info` VALUES ('40', 'emui.huawei.com', 'EMUI官网', 'http://www.emui.com/', null, '0', '0', '0', '0', 'admin', '2016-01-15 10:52:18', '43');
