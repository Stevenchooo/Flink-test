-- drop database if exists wdadb;
-- create database wdadb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use wdadb;

-- 用户表

DROP TABLE IF EXISTS `t_ms_user`;
CREATE TABLE `t_ms_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` char(50) NOT NULL COMMENT '用户名',
  `fullName` varchar(50) NOT NULL DEFAULT '',
  `department` varchar(100) DEFAULT '',
  `password` char(50) NOT NULL COMMENT '密码',
  `creator` char(50) DEFAULT '' COMMENT '创建人id',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastLoginTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后登录时间',
  `updatePwdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改密码的时间（密码过期时）',
  `status` char(10) NOT NULL DEFAULT 'actived' COMMENT '状态（actived激活，locked锁定，del删除）',
  `phoneNum` varchar(255) NOT NULL DEFAULT '' COMMENT '联系号码',
  `randNum` int(11) NOT NULL DEFAULT '0' COMMENT '随机数，用于用户找回密码时用，找的时候置随机数给用户，完成时销毁',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '电子邮箱',
  `oldPwd` varchar(255) NOT NULL DEFAULT '' COMMENT '老密码，最多纪录5个',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述',
  `isAdmin` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_ms_user
-- ----------------------------
INSERT INTO `t_ms_user` VALUES ('1', 'admin', 'admin', '', 'x0O6Na9VQpC_M7tIXNpeVdFM4-frML7lf5mETv8Q_CE', 'admin', '2015-08-06 10:19:18', '2016-01-27 19:54:20', '2017-03-05 16:50:32', 'actived', '', '0', '', '-5-JAnSWXH4G3tY1dtjfbTfeZrgD-SbVPfEVftXcfEB,LkB3Ts00s9x2rn5voGU-4Yn5ySa8hLE2OA46yB2HbKF,', '超级管理员', '1');

-- 删除用户的用户表
drop table if exists t_ms_deluser;
create table t_ms_deluser
(
    account              char(50) not null comment '用户名',
    
    creator              char(50) not null default '' comment '创建人id',
    createTime           timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
    lastLoginTime        timestamp not null default '0000-00-00 00:00:00' comment '最后登录时间',
    
    phoneNum             char(32) not null default '' comment '联系号码',
    
    email                varchar(255) not null default '' comment '电子邮箱',
    description          varchar(255) not null default '' comment '描述',
    
    index idx_account(account)
) ENGINE=InnoDB COMMENT='用户表' AUTO_INCREMENT=1;

-- 被锁定的用户，记录这个表中的用户，在过期时间之前不能登录
drop table if exists t_ms_lockuser;
CREATE TABLE t_ms_lockuser (
    type            char(5) NOT NULL DEFAULT 'user' COMMENT '锁定类型，ip与user两种' ,
    tryTimes        int(11) NOT NULL DEFAULT 0 COMMENT '尝试错误密码的次数' ,
    val             varchar(50) NOT NULL DEFAULT '' COMMENT '对应值' ,
    expireTime      timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '锁定失效时间点' ,
    PRIMARY KEY (val, type)
) ENGINE=InnoDB COMMENT='锁定用户表';

-- 控制左侧菜单树的结构，同时决定了权限中的范围
drop table if exists t_ms_model;
create table t_ms_model
(
    id                   int not null auto_increment comment '模型id',
    
    pid                  int not null default 0 comment '父群组id',
    meta                 char(50) not null default 'group' comment '模块类型',
    createTime           timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
    modifyTime           timestamp not null default '0000-00-00 00:00:00' comment '修改时间',
    visible              boolean not null default true comment '是否可见',
    creator              char(50) not null default '' comment '创建人',
    
    name                 varchar(255) not null default '' comment '名称',
    val                  varchar(255) not null default '' comment '值、描述',
    
    primary key(id),
    index idx_pidmeta(pid, meta),
    index idx_pidname(pid, name),
    index idx_metaval(val, meta),
    index idx_name(name)
) ENGINE=InnoDB COMMENT='模型表，定义整个系统数据的层次关系' AUTO_INCREMENT=1;

insert into t_ms_model(id, pid, meta, name, creator, val, visible) values
(1, 0, 'root', '/', 'admin', '系统模型的根', true),
(2, 1, 'group', 'config', 'admin', '配置组', true),
(4, 1, 'user', 'users', 'admin', '用户管理', true),
(5, 4, 'userList', 'userList', 'admin', '用户管理', true),
(6, 4, 'role', 'role', 'admin', '角色定义', true),
-- (6, 1, 'system', 'system', 'admin', '运行系统', true),
-- 超级管理员及权限,val中存放角色名
(7, 1, 'admin', 'admin', 'admin', 'root', false);

-- 元数据，定义了各种数据的图表、主管理界面url、下层可包含的元数据类型
drop table if exists t_ms_meta;
create table t_ms_meta
(
    isVisible            boolean not null default true comment '模块在树图中是否可见',
    isUnique             boolean not null default false comment '模块在同级树图中是否唯一',
    isRightCtrl          boolean not null default true comment '是否需要做权限控制',
    name                 char(50) not null default '' comment '名称，必须唯一',
    url                  varchar(255) not null default '',
    icon                 varchar(255) not null default '',
    subMetas             varchar(255) not null default '' comment '可以包含的子模块类型,格式：|admin|group|',
    description          varchar(255) not null default '' comment '描述',
    
    primary key(name)
) ENGINE=InnoDB COMMENT='元数据定义';

insert t_ms_meta(isVisible, isUnique, isRightCtrl, name, url, subMetas, description, icon) values
(true,  true, false, 'root', '', '|group|admin|', '模型的根', '/imgs/tree/root.gif'),
(true,  true, false, 'group', '/page/group', '|group|configItem|admin|', '配置组', ''),

(false, true, false, 'configItem', '/page/configItem', '', '配置项', ''),
(true, true, true, 'admin', '/page/admin', '', '管理员', ''),

-- 系统信息
-- (true,  true,  'system', '/page/system', '|service|admin|', '系统状态', '/imgs/tree/system.gif'),
-- (true,  true,  'service', '/page/service', '|server|service|admin|', '服务', '/imgs/tree/service.gif'),
-- (true,  false, 'server', '/page/server', '|cpu|mem|disk|', '服务器', '/imgs/tree/computer.gif'),

-- 监控项，绝大部分系统不需要
-- (false, true,  'mem', '/page/mem', '', '内存', ''),
-- (false, true,  'cpu', '/page/cpu', '', 'CPU', ''),
-- (false, true,  'disk', '/page/disk', '', '硬盘', '');

(true,  true, true, 'user', '/page/user', '|userList|role|', '用户管理', '/imgs/tree/users.gif'),
(false, true, true, 'userList', '/page/user', '', '用户管理', ''),
(false, true, true, 'role', '/page/role', '', '角色定义', '');

-- 角色名称
drop table if exists t_ms_role;
create table t_ms_role(
    role                 char(20) not null default '' comment '角色',
    name                 varchar(255) not null default '' comment '角色名称',
    description          varchar(255) not null default '' comment '描述',
    
    primary key(role)
) ENGINE=InnoDB COMMENT='角色';
insert into t_ms_role(role, name, description) values
('root', '超级管理员', '顶级管理员'),
('normal', '普通管理员', '普通管理员');

-- 角色权限定义，meta指定了对某一种数据的权限，权限分为crud（增删改查）
drop table if exists t_ms_right;
create table t_ms_right(
    role                 char(20) not null default '' comment '角色名称',
    meta                 char(50) not null default '' comment '能够操作的meta名称',
    oprRight             varchar(255) not null default '|r|' comment '可执行的操作类型',
    
    primary key(role, meta)
) ENGINE=InnoDB COMMENT='对数据的操作权限';
insert into t_ms_right(role, meta, oprRight) values
('root', '*', '|c|r|u|d|'),
('normal', '*', '|c|r|u|d|');

-- 日志，需要认证的接口，除了查询类的，都应该记录日志
drop table if exists t_ms_log;
create table t_ms_log
(
    account              char(50) not null default '',
    logTime              timestamp not null default CURRENT_TIMESTAMP,
    resultCode           int not null default 0 comment '执行结果',

    apiName              varchar(255) not null default 0, -- 0:add, 1:del, 2:modify, 3: query
    info                 varchar(2000) not null default '' comment '操作的sql记录',
    
    index idx_logTime(logTime, apiName, account)
) ENGINE=MyISAM COMMENT='操作日志';

insert into t_ms_log(account, apiName, resultCode, info)
values('admin', 'none', 0, '系统初始化');

-- 当前没有使用，作为后台系统监控的数据
drop table if exists t_ms_report;
create table t_ms_report
(
    reportItemId         int not null default 0 comment 'mem/cpu/disk对应的模型id',
    reportTime           timestamp not null default CURRENT_TIMESTAMP comment '上报时间',
    val                  varchar(255) not null default '' comment '上报值',
    
    primary key(reportItemId,reportTime)
) ENGINE=InnoDB COMMENT='报表';

