CREATE EXTERNAL TABLE ODS_UP_UL_REPORT_MESSAGE_DM (
timeStamp string comment '生成该条话单记录的时间戳',
transactionId string comment '会话id',
destAddr string comment '被叫号码',
orgAddr string comment '主叫号码',
recvTime string comment '接收回执短信时间',
msgID string comment '短信序列号',
stat string comment '状态结果',
result string comment '回执结果',
contentLength int comment '短信内容长度')
PARTITIONED BY (pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\u0001',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='u\0001')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/UP/ODS_UP_UL_REPORT_MESSAGE_DM' 
   
   
推送数据文件路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_UL_REPORT_MESSAGE_DM   

xml配置信息：

       <FileToHDFS action="ODS_UP_UL_REPORT_MESSAGE_DM">
        <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_UL_REPORT_MESSAGE_DM/*ODS_UP_UL_REPORT_MESSAGE_DM_*.log</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>360</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_UP_UL_REPORT_MESSAGE_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>UP/ODS_UP_UL_REPORT_MESSAGE_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
       </FileToHDFS>
