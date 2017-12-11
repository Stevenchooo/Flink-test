CREATE EXTERNAL TABLE ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM (
time string,
level string,
uid string,
msgid string,
type string,
channel string)
PARTITIONED BY (                                                       
   pt_d string,
   pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/PHONESERVICE/ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM'    
   
数据文件推送路径：   /MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM/ 

XML配置文件：

           <FileToHDFS action="ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM/*_ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM_*.log</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>24</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>PHONESERVICE/ODS_PHONESERVICE_IM_ARRIVAL_LOG_HM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>