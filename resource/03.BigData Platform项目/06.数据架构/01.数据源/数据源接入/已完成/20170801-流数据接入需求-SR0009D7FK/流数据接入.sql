流接入：
1, ODS_RCM_RAS_INTERFACE_LOG_HM
CREATE EXTERNAL TABLE ODS_RCM_RAS_INTERFACE_LOG_HM(
time_stamp string,
log_level string,
response_time string,
rest_api string,
dummy_field string,
remote_address string,
request_id string,
return_code string,
dummy_field string,
request_json string,
response_json string)
PARTITIONED BY (pt_d string,pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_RAS_INTERFACE_LOG_HM' 
   
   路径：ads_rcm_ras_interface_log_hm/ads_rcm_ras_interface_log_hm*.txt

流接入： 
2, ODS_RCM_RAS_RUN_LOG_HM
CREATE EXTERNAL TABLE ODS_RCM_RAS_RUN_LOG_HM (
time_stamp string,
severity string,
host string,
service string,
user string,
terminal string,
ip string,
domain string,
type string,
protocol string,
security_incident string,
result string,	
additional_info string)
PARTITIONED BY (pt_d string,pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_RAS_RUN_LOG_HM' 
   
   推送路径：ads_rcm_ras_run_log_hm/ads_rcm_ras_run_log_hm*.txt
 
 
 3, ODS_RCM_RAS_OPER_LOG_HM
CREATE EXTERNAL TABLE ODS_RCM_RAS_OPER_LOG_HM (
time_stamp string,
level string,
virtual_machine string,
service string,
transaction_id string,
Info string,
additional_info string)
PARTITIONED BY (pt_d string,pt_h string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\t',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='\t')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_RAS_OPER_LOG_HM' 
  
数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_RAS_OPER_LOG_HM 

XML配置文件：
           <FileToHDFS action="ODS_RCM_RAS_OPER_LOG_HM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_RCM_RAS_OPER_LOG_HM/*ODS_RCM_RAS_OPER_LOG_HM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_RCM_RAS_OPER_LOG_HM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>RCM/ODS_RCM_RAS_OPER_LOG_HM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

   
4, ODS_RCM_SCM_T_ACTIVITY_HS
CREATE EXTERNAL TABLE ODS_RCM_SCM_T_ACTIVITY_HS (
id string,
activity_name string,
activity_model_id string,
status string,
group_name string,
description string,
activity_model_content string,
priority string,
app_id string,
bs_id string,
aud_id string,
creater string,
created string,
modified string,
updater string,
traffictype string)
PARTITIONED BY (pt_d string,pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_SCM_T_ACTIVITY_HS' 

数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_ACTIVITY_HS 

XML配置文件：
           <FileToHDFS action="ODS_RCM_SCM_T_ACTIVITY_HS">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_RCM_SCM_T_ACTIVITY_HS/*ODS_RCM_SCM_T_ACTIVITY_HS_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_RCM_SCM_T_ACTIVITY_HS</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>RCM/ODS_RCM_SCM_T_ACTIVITY_HS</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
   
5, ODS_RCM_SCM_T_APP_HS
CREATE EXTERNAL TABLE ODS_RCM_SCM_T_APP_HS (
id string,
app_id string,
app_name string,
published string,
description string,
gray_artifact_group string,
live_artifact_group string,
test_artifact_group string,
creater string,
created string,
modified string,
updater string)
PARTITIONED BY (pt_d string,pt_h string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_SCM_T_APP_HS' 

数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_APP_HS 

XML配置文件：
           <FileToHDFS action="ODS_RCM_SCM_T_APP_HS">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_RCM_SCM_T_APP_HS/*ODS_RCM_SCM_T_APP_HS_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_RCM_SCM_T_APP_HS</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>RCM/ODS_RCM_SCM_T_APP_HS</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
   
6, ODS_RCM_SCM_T_BUSINESSSCENARIO_HS
CREATE EXTERNAL TABLE ODS_RCM_SCM_T_BUSINESSSCENARIO_HS (
id string,
scenario_name string,
app_id string,
rcmscenario_id string,
description string,
request_transformer_id string,
creater string,
created string,
modified string,
updater string,
tag string)
PARTITIONED BY (pt_d string,pt_h string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_SCM_T_BUSINESSSCENARIO_HS' 

数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_BUSINESSSCENARIO_HS 

XML配置文件：
           <FileToHDFS action="ODS_RCM_SCM_T_BUSINESSSCENARIO_HS">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_RCM_SCM_T_BUSINESSSCENARIO_HS/*ODS_RCM_SCM_T_BUSINESSSCENARIO_HS_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_RCM_SCM_T_BUSINESSSCENARIO_HS</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>RCM/ODS_RCM_SCM_T_BUSINESSSCENARIO_HS</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
   
7, ODS_RCM_SCM_T_RECOMMENDATION_HS
CREATE EXTERNAL TABLE ODS_RCM_SCM_T_RECOMMENDATION_HS (
id string,
name string,
description string,
created string,
last_modified string,
tag string,
model_id string,
creator string,
updator string,
bs_id string)
PARTITIONED BY (pt_d string,pt_h string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/RCM/ODS_RCM_SCM_T_RECOMMENDATION_HS' 

数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_RECOMMENDATION_HS 

XML配置文件：
           <FileToHDFS action="ODS_RCM_SCM_T_RECOMMENDATION_HS">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_RCM_SCM_T_RECOMMENDATION_HS/*ODS_RCM_SCM_T_RECOMMENDATION_HS_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_RCM_SCM_T_RECOMMENDATION_HS</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>RCM/ODS_RCM_SCM_T_RECOMMENDATION_HS</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

   
   
ODS_RCM_RAS_OPER_LOG_HM
数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_RAS_OPER_LOG_HM 
ODS_RCM_SCM_T_ACTIVITY_HS
数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_ACTIVITY_HS 
ODS_RCM_SCM_T_APP_HS
数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_APP_HS 
ODS_RCM_SCM_T_BUSINESSSCENARIO_HS
数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_BUSINESSSCENARIO_HS 
ODS_RCM_SCM_T_RECOMMENDATION_HS
数据文件推送路径：/MFS/DataIn/hadoop_NJ/odsdata/ODS_RCM_SCM_T_RECOMMENDATION_HS 