ODS_HWMOVIE_VOD_CONTENT_DM
新增字段：
alter table ODS_HWMOVIE_VOD_CONTENT_DM add columns (mediaID string comment '点播媒体文件编号');  

ODS_HWMOVIE_EPG_ACCESS_STAT_DM
新增字段：
alter table ODS_HWMOVIE_EPG_ACCESS_STAT_DM add columns (hvsSubNetId string comment 'HVS子网信息');  

ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM
新增字段：
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (session_id string comment '登录会话ID'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (user_agent string comment '终端HTTP请求的UserAgent'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (flag string comment 'Forthnet局点预留字段'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (download_type string comment 'V2R6C20定制版本预留字段'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (mediaCode string comment '播放记录的媒资Code'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (trickMode string comment '点播trick行为'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (vodUrl string comment 'VOD播放URL'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (seriesId string comment 'VOD父集ID'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (movieNum string comment 'VOD的连续剧集数'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (programCode string comment 'IR操作对应的节目单Code'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (cellId string comment 'eMBMS业务终端接入的小区ID'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (SAI string comment 'eMBMS业务区域'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (MAI string comment 'eMBMS无线同步区域ID'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (msisdn string comment '手机号'); 
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM add columns (hvsSubNetId string comment 'HVS子网信息'); 



