CREATE EXTERNAL TABLE ODS_DEV_CLOUD_FOLDER_PHONE_CONFIG_DS(                     
   series string,                                                
   product string,                                                   
   model string)                                                          
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_CLOUD_FOLDER_PHONE_CONFIG_DS'  
   
数据文件推送路径：   /MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_PHONE_CONFIG_DS
