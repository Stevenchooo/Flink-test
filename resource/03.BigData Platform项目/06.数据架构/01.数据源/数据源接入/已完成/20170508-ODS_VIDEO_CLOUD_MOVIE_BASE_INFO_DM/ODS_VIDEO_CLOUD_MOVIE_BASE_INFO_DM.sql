CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM (
timeStamp string,
actionType string,
mvId string,
title string,
alias string,
volumeSize int,
releaseTime int,
doubanId string,
updateVolume int,
payType int,
topCategory string,
categorys string,
state int,
areaInfos string,
casts string,
directors string,
writers string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM' 