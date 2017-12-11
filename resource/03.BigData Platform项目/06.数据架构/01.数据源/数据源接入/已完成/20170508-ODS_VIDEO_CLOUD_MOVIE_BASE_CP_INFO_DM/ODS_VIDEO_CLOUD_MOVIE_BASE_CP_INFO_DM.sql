CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM (
timeStamp string,
actionType string,
mvId String,
cpId int,
cpMvId String,
appTypes String,
times int,
definition int,
langs String,
state int,
copyRight String,
payType string,
price int)
PARTITIONED BY (                                                       
   pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM' 