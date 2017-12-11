ODS_TRADE_BANKCARD_NEW_DM
新增字段：
alter table ODS_TRADE_BANKCARD_NEW_DM add columns (APPLICATION_ID string comment '应用ID');  
alter table ODS_TRADE_BANKCARD_NEW_DM add columns (channel string comment '绑卡渠道');