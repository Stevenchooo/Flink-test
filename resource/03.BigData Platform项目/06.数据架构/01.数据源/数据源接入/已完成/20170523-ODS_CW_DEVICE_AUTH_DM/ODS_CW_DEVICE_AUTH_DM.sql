CREATE EXTERNAL TABLE ODS_CW_DEVICE_AUTH_DM (
user_name STRING,
aID STRING,
channel STRING,
service_type TINYINT,
model STRING,
client_version STRING,
product_id STRING,
auth_time BIGINT)
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'=',',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'=',')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/PREPARE/ODS_CW_DEVICE_AUTH_DM'  
