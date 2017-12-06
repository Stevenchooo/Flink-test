set @@autocommit=0;
drop procedure if exists initTaskIdStepIdSerial;
delimiter //
create procedure initTaskIdStepIdSerial()
begin
  	delete from tcc_taskid_serial;
	insert into tcc_taskid_serial select serviceId,taskType,max(serialNo) as serialNo  from (select floor(task_id/1000000) as serviceId,floor(task_id/10000)%100 as taskType,task_id%10000 as serialNo from tcc_task) temp group by serviceId,taskType;
	delete from tcc_stepid_serial;
	insert into tcc_stepid_serial select task_id,max(step_id) as serialNo from tcc_task_step group by task_id; 
end
//
delimiter ;

INSERT INTO operator_info VALUES ('system', 'system', 'system', 'hadoop', '系统管理员', '2012-06-21 16:59:57');
INSERT INTO os_user_info VALUES ('hadoop', 'hadoop', 'hadoop');
INSERT INTO role_defination VALUES ('系统管理员', '2012-06-28 16:58:04', '系统管理员');

INSERT INTO service_defination VALUES ('0', 'Dbank', 'Dbank', '5', '100', 'Dbank', '', '', '2012-06-19 11:21:04');
INSERT INTO service_defination VALUES ('1', '终端云', '终端云', '5', '100', '终端云', '', '', '2012-06-19 11:21:21');
INSERT INTO service_defination VALUES ('2', '天天浏览器', '天天浏览器', '5', '100', '天天浏览器', '', '', '2012-06-19 11:21:30');
INSERT INTO service_defination VALUES ('3', 'Hotalk', 'Hotalk', '5', '100', 'Hotalk', '', '', '2012-06-19 11:21:40');
INSERT INTO service_defination VALUES ('4', '智汇云', '智汇云', '5', '100', '智汇云', '', '', '2012-06-19 11:21:53');
INSERT INTO service_defination VALUES ('5', '天天家园', '天天家园(地址本,空间,天天秀)', '5', '100', '天天家园', '', '', '2012-06-19 11:22:42');
INSERT INTO service_defination VALUES ('6', '天天记事', '天天记事', '5', '100', '天天记事', '', '', '2012-06-19 11:22:56');
INSERT INTO service_defination VALUES ('7', '企业通讯', 'UC2(企业通讯)', '5', '100', '企业通讯', '', '', '2012-06-19 11:23:24');
INSERT INTO service_defination VALUES ('8', '云服务', 'HWS(云服务)', '5', '100', '云服务', '', '', '2012-06-19 11:23:48');
INSERT INTO service_defination VALUES ('9', '虚拟主机', '虚拟主机', '5', '100', '虚拟主机', '', '', '2012-06-19 11:23:56');
INSERT INTO service_defination VALUES ('10', '云桌面', '云桌面', '5', '100', '', '', '', '2012-06-19 11:24:07');
INSERT INTO service_defination VALUES ('11', 'S3虚拟存储', 'S3虚拟存储', '5', '100', '', '', '', '2012-06-19 11:24:17');
INSERT INTO service_defination VALUES ('12', 'WEBOS', 'WEBOS', '5', '100', '', '', '', '2012-06-19 11:24:25');
INSERT INTO service_defination VALUES ('13', 'VoIP', 'VoIP', '5', '100', '', '', '', '2012-06-19 11:24:39');
INSERT INTO service_defination VALUES ('14', '天天微讯', '天天微讯', '5', '100', '', '', '', '2012-06-19 11:24:47');
INSERT INTO service_defination VALUES ('15', '网盘', '网盘', '5', '100', '', '', '', '2012-06-19 11:24:55');
INSERT INTO service_defination VALUES ('16', '相册', '相册', '5', '100', '', '', '', '2012-06-19 11:25:02');
INSERT INTO service_defination VALUES ('17', 'Phone+', 'Phone+', '5', '100', '', '', '', '2012-06-19 11:28:44');
INSERT INTO service_defination VALUES ('18', 'Push', 'Push', '5', '100', '', '', '', '2012-06-19 11:29:05');
INSERT INTO service_defination VALUES ('19', '游戏平台', '游戏平台', '5', '100', '', '', '', '2012-06-19 11:29:15');
INSERT INTO service_defination VALUES ('20', '手机支付', '手机支付', '5', '100', '', '', '', '2012-06-19 11:29:24');
INSERT INTO service_defination VALUES ('21', 'SNS', 'SNS', '5', '100', '', '', '', '2012-06-19 11:29:34');
INSERT INTO service_defination VALUES ('22', 'emotion论坛', 'emotion论坛', '6', '100', '', '', '', '2012-06-19 17:07:33');
INSERT INTO service_defination VALUES ('90', '智汇云开发者', '智汇云开发者', '5', '100', '', '', '', '2012-06-19 11:30:04');
INSERT INTO service_defination VALUES ('999', '全业务', '全业务', '5', '100', '', '', '', '2012-06-19 11:30:13');


call initTaskIdStepIdSerial();
drop procedure if exists initTaskIdStepIdSerial;
set @@autocommit=1;