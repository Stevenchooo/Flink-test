ODS_PUSH_CAMPAIGN_ADID_TASKID_DM
   CREATE EXTERNAL TABLE ODS_PUSH_CAMPAIGN_ADID_TASKID_DM(                     
   adId string,                                                
   taskId string,                                                   
   appId string,
   msgStyle string,                                                
   receiveTime string,                                                   
   sendDate string,
   sendChannel int,
   marketingType string )                                                          
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
   'hdfs://hacluster/AppData/BIProd/ODS/PUSH/ODS_PUSH_CAMPAIGN_ADID_TASKID_DM' 