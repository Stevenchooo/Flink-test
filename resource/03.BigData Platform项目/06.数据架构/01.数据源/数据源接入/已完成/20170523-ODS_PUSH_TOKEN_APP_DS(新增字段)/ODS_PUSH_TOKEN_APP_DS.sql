删除表：drop table ODS_PUSH_TOKEN_APP_DS;

创建表：ODS_PUSH_TOKEN_APP_DS

CREATE EXTERNAL TABLE ODS_PUSH_TOKEN_APP_DS (
provider_id int,
app_id string,
package_name string,
appname string,
polling_mode int,
createdate string,
isfree string,
apptype int,
user_id string,
dev_app_id string,
appCompressId string)
ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\u0001',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='\u0001')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/hadoop-NJ/data/ODS/PUSH/ODS_PUSH_TOKEN_APP_DS'  
