use usermanager;

insert into role_info(role_name,`desc`,create_time) values('系统管理员','系统管理员角色',now());
insert into role_info(role_name,`desc`,create_time) values('NONE','无权限角色',now());

insert into operator_info values('system','54b53072540eeeb8f8e9343e71f28176','系统管理员',now(),1);