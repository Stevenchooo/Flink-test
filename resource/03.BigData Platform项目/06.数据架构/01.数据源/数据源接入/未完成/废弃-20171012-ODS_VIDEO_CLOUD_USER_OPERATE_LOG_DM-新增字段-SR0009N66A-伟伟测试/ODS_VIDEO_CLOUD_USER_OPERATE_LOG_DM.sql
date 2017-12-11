ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM
alter table ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM  add columns (beId STRING COMMENT '默认为华为视频中国区运营渠道标识');
alter table ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM  add columns (timeStampUTC STRING COMMENT '操作时产生的UTC时间');