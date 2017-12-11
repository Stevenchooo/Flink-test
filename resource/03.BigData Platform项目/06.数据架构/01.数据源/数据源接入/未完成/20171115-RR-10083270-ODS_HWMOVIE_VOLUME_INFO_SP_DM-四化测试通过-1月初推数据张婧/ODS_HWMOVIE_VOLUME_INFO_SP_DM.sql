CREATE EXTERNAL TABLE ODS_HWMOVIE_VOLUME_INFO_SP_DM(
volumeid string comment '聚合剧集ID',
definition int comment '清晰度',
spid int comment '对应SP ID',
url string comment '播放地址',
onlinetime string comment '上线时间。',
activetime string comment '播放地址有效期。',
duration int comment '播放时长',
titlesflag int comment '片头',
tailleaderflag int comment '片尾',
preview int comment '预览时长',
mediaid string comment '媒质ID',
`format` string comment '媒质类型',
contentcode string comment '内容code',
spvolumeid string comment 'SP的剧集ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_VOLUME_INFO_SP_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_VOLUME_INFO_SP_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_VOLUME_INFO_SP_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_VOLUME_INFO_SP_DM/*_ODS_VIDEO_HIMOVIE_VOLUME_INFO_SP_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_VOLUME_INFO_SP_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_VOLUME_INFO_SP_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>