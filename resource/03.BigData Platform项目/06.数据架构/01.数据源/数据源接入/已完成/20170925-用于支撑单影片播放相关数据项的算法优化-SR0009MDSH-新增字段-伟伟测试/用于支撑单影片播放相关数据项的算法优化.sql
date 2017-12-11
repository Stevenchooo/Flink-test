ODS_HWMOVIE_REL_IPTV_CHANNEL_DM
新增字段：
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (code string comment '外系统主键');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (contentType int comment '频道类别');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (commercialTime string comment '内容商用时间');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (supplyLang string comment '频道能支持的语言描述');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (subtitleLang string comment '频道能支持的对白语种');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (starttime string comment '开始时间');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (endtime string comment '结束时间');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (searchCode string comment '搜索码');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (isDelete int comment '是否已经删除');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (DEVICETYPEGROUPS string comment '设备类型分组');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (createTime string comment '创建时间');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (UpdateTime string comment '更新时间');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (createTimeUTC string comment '创建时间UTC时间');  
alter table ODS_HWMOVIE_REL_IPTV_CHANNEL_DM add columns (UpdateTimeUTC string comment '更新时间的UTC时间');  


ODS_HWMOVIE_REL_IPTV_VOD_DM
新增字段：
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (code string comment '第三方内容系统的内容编号');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (contentType int comment '内容类型');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (Starttime string comment '影片有效开始时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (Endtime string comment '影片有效结束时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (commercialTime string comment '内容商用时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (supplyLang string comment 'VOD能支持的语言描述');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (produceDate string comment '出品日期描述');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (sdpcontentType string comment 'sdp上内容类型');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (recommend int comment '是否强档推荐');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (productZone string comment '内容出产地');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (deviceTypeGroups string comment '设备类型分组');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (subtitleLang string comment 'VOD能支持的对白语种');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (assertID string comment '内容提供商assertID');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (Product string comment 'ADI接口中对应的Product');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (searchCode string comment '搜索码');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (ActionTime string comment '操作时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (STARTTIMEUTC string comment '影片有效UTC开始时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (ENDTIMEUTC string comment '影片有效UTC结束时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (COMMERCIALTIMEUTC string comment '内容商用UTC时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (PRODUCEDATEUTC string comment '出品UTC日期描述');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (CMSCONTENTTYPE string comment 'CMS系统传入的内容类型');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (VODPRICE string comment 'VOD价格');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (POSTERPATH string comment '海报位置');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (OPERATORID string comment '创建VOD的操作员ID');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (FOREIGNSN string comment 'VOD外接的CMS的内容编号');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (VODNUM int comment 'VOD集数');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (CREATETIME string comment '创建时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (CREATETIMEUTC string comment '创建的UTC时间');  
alter table ODS_HWMOVIE_REL_IPTV_VOD_DM add columns (definitionFlag int comment '内容清晰度标识');  
