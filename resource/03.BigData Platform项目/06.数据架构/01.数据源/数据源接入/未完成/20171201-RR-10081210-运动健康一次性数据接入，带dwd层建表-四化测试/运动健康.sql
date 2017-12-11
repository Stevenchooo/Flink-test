CREATE EXTERNAL TABLE DWD_HOMECLOUD_HEALTH_USER_LEVEL_INFO_DS(
user_id string comment '�û�uid����',
day_level string comment '�ۼƴ������',
kakaSumValue string comment '���ܺ�Ŀ�����ֵ'
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
user_id string comment '�û�uid����',
medal_type string comment 'ѫ������',
medal_level int comment 'ѫ�½��',
take_Date string comment 'ѫ�»����'
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
user_id string comment '�û�uid����',
first_date string comment '�����˶���',
last_date int comment 'ĩ���˶���',
sum_steps string comment '�ۼƲ���',
sumDistance string comment '�ۼ����',
sumKcal string comment '�ۼƿ�·��',
maxSteps string comment '������ಽ��',
maxDistance string comment '��켣�˶�',
maxPace string comment '�������'
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