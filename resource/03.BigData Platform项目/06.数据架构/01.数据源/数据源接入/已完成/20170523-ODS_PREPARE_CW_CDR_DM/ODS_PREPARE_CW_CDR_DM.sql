CREATE EXTERNAL TABLE ODS_PREPARE_CW_CDR_DM (
cdr_id BIGINT,
cdr_type TINYINT,
account_id STRING,
user_id STRING,
transaction_id STRING,
session_id STRING,
begin_time BIGINT,
end_time BIGINT,
price_value INT,
real_value INT,
finish_reason TINYINT,
present_source STRING,
create_time STRING,
ssid STRING,
wlan_type INT,
channel STRING,
assuredflow BIGINT,
unassuredflow BIGINT,
wID STRING,
actual_value INT,
giftcode STRING,
desczh STRING)
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
   'hdfs://hacluster/AppData/BIProd/ODS/PREPARE/ODS_PREPARE_CW_CDR_DM'  
   

