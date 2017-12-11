CREATE EXTERNAL TABLE DWD_HOMECLOUD_HEALTH_USER_LEVEL_INFO_DS(
user_id string comment '用户uid主键',
day_level string comment '累计达标天数',
kakaSumValue string comment '加密后的卡卡总值'
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='|', 
   'line.delim'='\n',
   'serialization.format'='|')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/DWD/REF/DWD_HOMECLOUD_HEALTH_USER_LEVEL_INFO_DS'
   
   
CREATE EXTERNAL TABLE DWD_HOMECLOUD_HEALTH_USER_MEDAL_DS (
user_id string comment '用户uid主键',
medal_type string comment '勋章类型',
medal_level int comment '勋章界别',
take_Date string comment '勋章获得日'
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='|', 
   'line.delim'='\n',
   'serialization.format'='|')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/DWD/REF/DWD_HOMECLOUD_HEALTH_USER_MEDAL_DS'
   
   
   
CREATE EXTERNAL TABLE DWD_HOMECLOUD_HEALTH_USER_ACHIEVEMENT_DS (
user_id string comment '用户uid主键',
first_date string comment '初次运动日',
last_date int comment '末次运动日',
sum_steps string comment '累计步数',
sumDistance string comment '累计里程',
sumKcal string comment '累计卡路里',
maxSteps string comment '单日最多步数',
maxDistance string comment '最长轨迹运动',
maxPace string comment '最佳配速'
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='|', 
   'line.delim'='\n',
   'serialization.format'='|')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/DWD/REF/DWD_HOMECLOUD_HEALTH_USER_ACHIEVEMENT_DS' 