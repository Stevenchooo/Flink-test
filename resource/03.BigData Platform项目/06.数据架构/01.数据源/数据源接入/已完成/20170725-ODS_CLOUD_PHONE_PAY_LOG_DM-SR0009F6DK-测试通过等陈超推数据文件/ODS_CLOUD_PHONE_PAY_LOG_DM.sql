CREATE EXTERNAL TABLE ODS_CLOUD_PHONE_PAY_LOG_DM (
logTime String comment '日志打印时间',
logLevel String comment '日志级别',
interfaceName String comment '接口方法',
request String comment '请求消息允许打印的关键参数')
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
   'hdfs://hacluster/AppData/BIProd/ODS/CLOUD/ODS_CLOUD_PHONE_PAY_LOG_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_CLOUD_PHONE_PAY_LOG_DM   

XML配置文件：

           <FileToHDFS action="ODS_CLOUD_PHONE_PAY_LOG_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_CLOUD_PHONE_PAY_LOG_DM/*ODS_CLOUD_PHONE_PAY_LOG_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_CLOUD_PHONE_PAY_LOG_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>CLOUD/ODS_CLOUD_PHONE_PAY_LOG_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>