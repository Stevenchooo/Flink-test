CREATE EXTERNAL TABLE ODS_HWMOVIE_MV_BASE_INFO_SP_DM(
mvid string comment '聚合后的影片id',
spid int comment 'Sp服务商ID',
spmvid string comment '内容提供商原始影片节目ID',
apptypes string comment '最大集数',
copyright string comment '版权授权地区',
trytype int comment '试看类型',
trytime  string comment '试看时效',
effectivefrom string comment '授权开始时间',
expirationat string comment '授权结束时间',
cpid string comment '内容提供商ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_MV_BASE_INFO_SP_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_MV_BASE_INFO_SP_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_MV_BASE_INFO_SP_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_MV_BASE_INFO_SP_DM/*_ODS_VIDEO_HIMOVIE_MV_BASE_INFO_SP_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_MV_BASE_INFO_SP_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_MV_BASE_INFO_SP_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>