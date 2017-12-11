ODS_HOTA_UPDATE_DEVICEINFO_DM_NEW
新增字段：
alter table ODS_HOTA_UPDATE_DEVICEINFO_DM_NEW add columns (RouterFeature string comment '路由特性信息'); 

ODS_HOTA_UPDATE_LOG_DM_NEW
alter table ODS_HOTA_UPDATE_LOG_DM_NEW change VersionID VersionID string comment '版本ID';
