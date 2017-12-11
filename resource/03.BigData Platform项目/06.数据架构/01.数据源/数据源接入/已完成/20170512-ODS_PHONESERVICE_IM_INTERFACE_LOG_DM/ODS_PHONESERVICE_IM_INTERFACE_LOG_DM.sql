CREATE EXTERNAL TABLE ODS_PHONESERVICE_IM_INTERFACE_LOG_DM (
`TimeStamp` string,
LogLevel string,
ResTime string,
type string,
name string,
src string,
dest string,
traceId string,
transId string,
ReturnCode string,
ReturnInfo string,
extinfo string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/PHONESERVICE/ODS_PHONESERVICE_IM_INTERFACE_LOG_DM'    
   
数据文件推送路径：   /MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_IM_INTERFACE_LOG_DM