CREATE EXTERNAL TABLE ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM(
columnid string comment '栏目ID',
contenttype int comment '内容类型',
contentid string comment '管理的对象的id',
filtertype int comment '内容的过滤类型',
minversion int comment '开始版本号',
maxversion int comment '结束版本号',
terminalid string comment '机型过滤机型列表',
position int comment '排序的位置',
status int comment '状态',
auditstatus int comment '审核状态',
extid string comment 'MVID字段')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM/*_ODS_VIDEO_HIMOVIE_COLUMN_CONTENT_RELATION_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_COLUMN_CONTENT_RELATION_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>