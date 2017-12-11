CREATE EXTERNAL TABLE ODS_MUSIC_PROMOTION_CDR_DM (
campid String comment '活动ID',
adpositionid String comment '活动广告位ID',
userid String comment '用户ID',
ip String comment '用户IP',
operationtime String comment '话单上报时间',
acttype String comment '话单类型',
terminalid String comment '终端ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/MUSIC/ODS_MUSIC_PROMOTION_CDR_DM' 

   数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_PROMOTION_CDR_DM   

XML配置文件：

           <FileToHDFS action="ODS_MUSIC_PROMOTION_CDR_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_PROMOTION_CDR_DM/*ODS_MUSIC_PROMOTION_CDR_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_MUSIC_PROMOTION_CDR_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>MUSIC/ODS_MUSIC_PROMOTION_CDR_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>