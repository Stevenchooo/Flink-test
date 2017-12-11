CREATE EXTERNAL TABLE ODS_HWMOVIE_COLUMN_INFO_DM(
id string comment '栏目ID',
beid string comment '渠道ID',
appgroupid string comment '栏目挂载的AppGroup的ID',
`type` int comment '栏目类型',
mounttype int comment '栏目挂载类型',
parentid string comment '所在的父栏目id',
vipclassid int comment '会员id',
templateid int comment '终端显示调用的相应模板编号',
recommandtype int comment '推荐栏目的推荐类型',
isremovable int comment '位置是否可以被移动',
status int comment '状态',
auditstatus int comment '审核状态',
filterid string comment '筛选条件',
method string comment '频道类型')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_COLUMN_INFO_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_COLUMN_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_COLUMN_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_COLUMN_INFO_DM/*_ODS_VIDEO_HIMOVIE_COLUMN_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_COLUMN_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_COLUMN_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>