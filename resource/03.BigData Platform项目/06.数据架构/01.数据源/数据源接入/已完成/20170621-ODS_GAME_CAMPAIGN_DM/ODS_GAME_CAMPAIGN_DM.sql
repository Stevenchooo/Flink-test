CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_DM (
id string comment '活动ID',
name string comment '活动名称',
type int comment '活动类型',
startDate string comment '活动开始时间',
enddate string comment '活动结束时间',
auditStatus int comment '活动状态')
PARTITIONED BY (pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_DM' 
   
   
推送数据文件路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_DM



xml配置信息：

       <FileToHDFS action="ODS_GAME_CAMPAIGN_DM">
        <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_DM/*_ODS_GAME_CAMPAIGN_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>360</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_CAMPAIGN_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
       </FileToHDFS>   
