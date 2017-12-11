CREATE EXTERNAL TABLE ODS_HISPACE_APP_TV_DM (
appid string comment '应用在应用市场分配的ID',
ageclass string comment '限制级别',
likecounts string comment '点赞数',
apptype string comment '应用类型',
appnettype int comment '应用联网类型',
appoid string comment '新闻的唯一标识',
tvtype string comment '日志数据',
appadapters string comment '应用适配',
appadapters_op string comment '应用适配')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HISPACE/ODS_HISPACE_APP_TV_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_APP_TV_DM   

XML配置文件：

           <FileToHDFS action="ODS_HISPACE_APP_TV_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_APP_TV_DM/*ODS_HISPACE_APP_TV_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HISPACE_APP_TV_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HISPACE/ODS_HISPACE_APP_TV_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
