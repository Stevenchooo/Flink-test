CREATE EXTERNAL TABLE ODS_HWMOVIE_REL_IPTV_VOD_HM(
vodID bigint comment 'VOD编号',
vodType bigint comment 'VOD类型',
sp string comment '节目提供商代码',
Company string comment '内容的发行公司',
tags string comment '关联标签，无多语种',
GENREIDS string comment 'Genreid以英文逗号分隔',
ShowType string comment '流派',
releaseYear string comment '上映年份',
reviewScore string comment '评分',
actionType bigint comment '操作类型',
ACTIONTIMEUTC string comment '操作时间')
PARTITIONED BY (                                                       
   pt_d string,pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_REL_IPTV_VOD_HM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_REL_IPTV_VOD_HM 

XML配置文件：

           <FileToHDFS action="ODS_HWMOVIE_REL_IPTV_VOD_HM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_REL_IPTV_VOD_HM/*OODS_HWMOVIE_REL_IPTV_VOD_HM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWMOVIE_REL_IPTV_VOD_HM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_REL_IPTV_VOD_HM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
CREATE EXTERNAL TABLE ODS_HWMOVIE_REL_IPTV_VOD_DM(
vodID bigint comment 'VOD编号',
vodType bigint comment 'VOD类型',
sp string comment '节目提供商代码',
Company string comment '内容的发行公司',
tags string comment '关联标签，无多语种',
GENREIDS string comment 'Genreid以英文逗号分隔',
ShowType string comment '流派',
releaseYear string comment '上映年份',
reviewScore string comment '评分',
actionType bigint comment '操作类型',
ACTIONTIMEUTC string comment '操作时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_REL_IPTV_VOD_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_REL_IPTV_VOD_DM 

XML配置文件：

           <FileToHDFS action="ODS_HWMOVIE_REL_IPTV_VOD_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_REL_IPTV_VOD_DM/*ODS_HWMOVIE_REL_IPTV_VOD_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWMOVIE_REL_IPTV_VOD_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_REL_IPTV_VOD_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>