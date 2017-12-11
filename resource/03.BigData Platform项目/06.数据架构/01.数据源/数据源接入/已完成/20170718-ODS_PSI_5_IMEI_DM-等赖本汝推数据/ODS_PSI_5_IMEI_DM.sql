ODS_PSI_5_IMEI_DM
新增字段：
alter table ODS_PSI_5_IMEI_DM add columns (MAC1 string comment '加密后的MAC地址');  
alter table ODS_PSI_5_IMEI_DM add columns (MAC2 string comment '加密后的MAC地址');  
alter table ODS_PSI_5_IMEI_DM add columns (SN string comment '加密的SN码');  
alter table ODS_PSI_5_IMEI_DM add columns (STATUS string comment '串码的状态');  