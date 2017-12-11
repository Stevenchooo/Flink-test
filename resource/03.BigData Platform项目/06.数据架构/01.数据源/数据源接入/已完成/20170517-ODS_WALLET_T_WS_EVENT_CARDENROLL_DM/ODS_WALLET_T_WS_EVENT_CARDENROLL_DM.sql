CREATE EXTERNAL TABLE ODS_WALLET_T_WS_EVENT_CARDENROLL_DM(
eventid string,
time string,
uid string,
issuerid string,
cardType string,
terminal string,
result string,
errorDesc string,
createTime string)
PARTITIONED BY (                                                       
   pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_T_WS_EVENT_CARDENROLL_DM' 
   
数据文件推送路径：   /MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_T_WS_EVENT_CARDENROLL_DM/