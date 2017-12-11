ODS_HOMECLOUD_HEALTH_USER_ACTIVITY_DM
新增字段：
alter table ODS_HOMECLOUD_HEALTH_USER_ACTIVITY_DM add columns (completeTimes string comment '活动完成次数');  
alter table ODS_HOMECLOUD_HEALTH_USER_ACTIVITY_DM add columns (campaignTimes string comment '活动剩余抽奖次数');  