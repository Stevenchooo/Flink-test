CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM (
timeStamp string,
actionType string,
cpMvId string,
cpId int,
cpVolumeId string,
volumeIndex int,
url string,
mediaId	string,
format string,
state int,
definition int)
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM'
   