
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `file_upload_table`
-- ----------------------------
DROP TABLE IF EXISTS `file_upload_table`;
CREATE TABLE `file_upload_table` (
  `file_id` varchar(20) NOT NULL,
  `load_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `flag` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
