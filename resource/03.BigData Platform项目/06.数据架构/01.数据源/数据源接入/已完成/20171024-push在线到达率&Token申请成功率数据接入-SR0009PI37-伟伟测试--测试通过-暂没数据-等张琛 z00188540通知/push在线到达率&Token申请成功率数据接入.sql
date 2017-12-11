CREATE EXTERNAL TABLE ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM (
time string comment '时间',
event_time string comment '事件时间',
net_type string comment '网络类型',
event_id string comment '事件ID',
event_info string comment '事件附加信息',
agent_verion string comment 'Agent版本号',
aes_deviceId string comment 'AES128加密的设备ID',
sha_deviceId string comment 'SHA256加密的设备ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/PUSH/ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM   

XML配置文件：

           <FileToHDFS action="ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM/*ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>35</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>PUSH/ODS_PUSH_AUTH_CLIENT_REPORT_LOG_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
CREATE EXTERNAL TABLE ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM (
time string comment '时间',
sha_deviceId string comment 'SHA256加密的设备ID',
aes_deviceId string comment 'AES128加密的设备ID',
model string comment '机型',
agent_verion string comment 'Agent版本号',
emuiVersion string comment 'EMUI版本号',
groupId string comment '采样组ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/PUSH/ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM   

XML配置文件：

           <FileToHDFS action="ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM/*ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>35</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>PUSH/ODS_PUSH_TRS_TARGET_DEVICE_LOG_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>