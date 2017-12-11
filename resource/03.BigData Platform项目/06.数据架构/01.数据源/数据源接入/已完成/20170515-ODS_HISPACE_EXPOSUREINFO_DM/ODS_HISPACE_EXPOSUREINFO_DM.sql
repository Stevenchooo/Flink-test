CREATE EXTERNAL TABLE ODS_HISPACE_EXPOSUREINFO_DM (
`datetime` String comment '记录时间',
logonid String comment '客户端参数编码',
sessionId String comment '会话id',
layoutId String comment '布局ID',
ts String comment '客户端记录的时间戳',
detailid String comment '详情id，即卡片或者是应用点击进入详情是访问云端的uri',
accountid String comment '华为帐号id',
trace String comment '来源追溯字段')
PARTITIONED BY (pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/HISPACE/ODS_HISPACE_EXPOSUREINFO_DM' 
   
   
   
推送数据文件路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_EXPOSUREINFO_DM   
