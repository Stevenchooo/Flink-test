ODS_CLOUDPHOTO_LOG_DM
新增字段：
alter table ODS_CLOUDPHOTO_LOG_DM add columns (extend_6 string comment '下载节点');  
alter table ODS_CLOUDPHOTO_LOG_DM add columns (extend_7 string comment '短链接');  
alter table ODS_CLOUDPHOTO_LOG_DM add columns (extend_8 string comment '资源类型');  
alter table ODS_CLOUDPHOTO_LOG_DM add columns (extend_9 string comment '分类Id');  
alter table ODS_CLOUDPHOTO_LOG_DM add columns (extend_10 string comment '标签ID'); 

ODS_CLOUD_PHONE_OPER_LOG_DM
alter table ODS_CLOUD_PHONE_OPER_LOG_DM change extend code string comment '接口响应码';
alter table ODS_CLOUD_PHONE_OPER_LOG_DM add columns (info string comment '接口响应描述信息'); 
alter table ODS_CLOUD_PHONE_OPER_LOG_DM add columns (Extend string comment '每个接口自身扩展信息，见事件说明详情,extend字段：如果只有一个字段时，直接拼接在后面，如果有多个字段，用JSON表示');


 