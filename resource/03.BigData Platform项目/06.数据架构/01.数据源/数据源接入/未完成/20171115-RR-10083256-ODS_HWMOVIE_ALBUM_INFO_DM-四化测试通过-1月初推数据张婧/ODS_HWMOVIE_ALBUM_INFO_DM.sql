CREATE EXTERNAL TABLE ODS_HWMOVIE_ALBUM_INFO_DM(
albumid string comment '专辑ID',
beid string comment '渠道ID',
appid string comment '专辑应用类型',
templateid int comment '终端显示调用的相应模板编号',
status int comment '状态',
auditstatus int comment '审核状态')
PARTITIONED BY ( pt_d string )
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_ALBUM_INFO_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_ALBUM_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_ALBUM_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_ALBUM_INFO_DM/*_ODS_HWMOVIE_ALBUM_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_ALBUM_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_ALBUM_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>