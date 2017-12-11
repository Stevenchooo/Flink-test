新接入
CREATE EXTERNAL TABLE ODS_DEV_GLOBAL_APP_APK_DM (
appID bigint ,
pkgVersion bigint ,
userID bigint ,
fileName string ,
versionCode bigint ,
versionNumber string ,
packagePath string ,
packageSize bigint ,
screenResolution string ,
appInnerFree bigint ,
activeState string ,
sensitivePermissionList string ,
sensitivePermissionDesc string ,
appNameFromApk string )
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
   'hdfs://hacluster/AppData/OpenAllianceProd/OpenAlliance/data/ODS/DEV/ODS_DEV_GLOBAL_APP_APK_DM'  
   
数据文件推送路径：   /MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_GLOBAL_APP_APK_DM/ 
 
 新接入
CREATE EXTERNAL TABLE ODS_DEV_GLOBAL_APP_LANGUAGE_DM (
appID bigint ,
userID bigint ,
serialNo bigint ,
language string ,
appName string ,
appDesc string ,
briefInfo string ,
newFeatures string ,
appIcon string ,
screenShots string ,
promoGraphics string ,
banner string ,
showType bigint )
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
   'hdfs://hacluster/AppData/OpenAllianceProd/OpenAlliance/data/ODS/DEV/ODS_DEV_GLOBAL_APP_LANGUAGE_DM'  
   
数据文件推送路径：   /MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_GLOBAL_APP_LANGUAGE_DM/ 
 
新接入 
CREATE EXTERNAL TABLE ODS_DEV_GLOBAL_APP_DM (
serialNo bigint ,
appID bigint ,
oldAppID bigint ,
oldCloudFolderAppID bigint ,
maxID bigint ,
userID bigint ,
prodState bigint ,
activeFlag bigint ,
intactFlag bigint ,
appName string ,
defaultLang string ,
packageName string ,
parentType bigint ,
childType string ,
grandChildType string ,
officialWebsite string ,
contactInfo string ,
privacyPolicy string ,
certificateURLs string ,
appKeywords string ,
appAttrs string ,
createTime string ,
updateTime string ,
appNetType bigint ,
hiddenPriPolicy bigint ,
lockFlag bigint ,
tvRemoteControl string ,
webAppKey string ,
appAdapters string ,
isPushActivity bigint ,
webAppType string ,
webAppUrl string )
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
   'hdfs://hacluster/AppData/OpenAllianceProd/OpenAlliance/data/ODS/DEV/ODS_DEV_GLOBAL_APP_DM'   
   
数据文件推送路径：   /MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_GLOBAL_APP_DM/ 
 
已存在 
CREATE EXTERNAL TABLE ODS_DEV_GLOBAL_PRODUCT_UP_DM (
prodID string ,
parentType string ,
userID string ,
prodName string ,
serialNo string ,
prodState string ,
activeFlag string ,
intactFlag string ,
createTime string ,
updateTime string ,
multiSrvFlag string ,
prodType string ,
createCountry string )
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
   'hdfs://hacluster/AppData/OpenAllianceProd/OpenAlliance/data/ODS/DEV/ODS_DEV_GLOBAL_PRODUCT_UP_DM'    
   
数据文件推送路径：   /MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_GLOBAL_PRODUCT_UP_DM/ 
 
 已存在 
CREATE EXTERNAL TABLE ODS_DEV_GLOBAL_SERVICE_UP_DM (
prodID string ,
parentType string ,
maxID string ,
prodSerialNo string ,
lastSrvSerialNo string ,
srvSerialNo string ,
serviceItem string ,
openState string ,
serviceAttrs string   ,
userID string ,
createTime string ,
updateTime string ,
serviceState string ,
offShelfReason string ,
lastAuditedUserID string ,
lastAuditedTime string ,
lastAuditResult string ,
auditAttachment string ,
sendMail string ,
pkgVersion string )
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
   'hdfs://hacluster/AppData/OpenAllianceProd/OpenAlliance/data/ODS/DEV/ODS_DEV_GLOBAL_SERVICE_UP_DM'    
   
数据文件推送路径：   /MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_GLOBAL_SERVICE_UP_DM/ 

