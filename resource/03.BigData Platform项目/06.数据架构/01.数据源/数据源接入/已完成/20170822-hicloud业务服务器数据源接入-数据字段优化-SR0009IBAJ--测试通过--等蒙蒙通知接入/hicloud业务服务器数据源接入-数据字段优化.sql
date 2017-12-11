ODS_CLOUD_PHONE_OPER_LOG_DM  
新增字段：
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (traceId string comment '事务跟踪ID');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (useTime string comment '接口耗时单位ms');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (starttime string comment '接口调用开始时间单位ms');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (Model string comment '机型');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (romvision string comment 'ROM版本号');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (appvision string comment '客户端版本号');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (Reqsize string comment '请求报文大小');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (Rspsize string comment '响应报文大小');  
alter table ODS_CLOUD_PHONE_OPER_LOG_DM  add columns (Logversion string comment '日志版本号'); 


ODS_CLOUDPHOTO_LOG_DM  
字段变更：
alter table ODS_CLOUDPHOTO_LOG_DM change api_name name comment'事件名称';
alter table ODS_CLOUDPHOTO_LOG_DM change response_id code comment'接口响应码';
alter table ODS_CLOUDPHOTO_LOG_DM change response_desc info comment'接口响应描述信息';
alter table ODS_CLOUDPHOTO_LOG_DM change ip extend comment'扩展信息';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_1 traceId comment'事务跟踪ID';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_2 useTime comment'接口耗时单位ms';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_3 starttime comment'接口调用开始时间单位ms';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_4 Model comment'机型';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_5 romvision comment'ROM版本号';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_6 appvision comment'客户端版本号';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_7 Reqsize comment'请求报文大小';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_8 Rspsize comment'响应报文大小';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_9 Logversion comment'日志版本号';
alter table ODS_CLOUDPHOTO_LOG_DM change extend_10 extend_10 comment'此字段暂时不用';




