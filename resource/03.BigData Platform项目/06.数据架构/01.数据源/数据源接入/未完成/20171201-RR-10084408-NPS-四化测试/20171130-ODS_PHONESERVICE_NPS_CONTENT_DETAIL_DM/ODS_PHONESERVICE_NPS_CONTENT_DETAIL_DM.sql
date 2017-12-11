CREATE EXTERNAL TABLE ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM(
id string comment '主键',
keyword string comment '关键字',
nps_id string comment '问卷业务',
nps_name string comment '问卷业务名称',
content string comment '问卷内容',
content_desc string comment '问卷描叙',
priority string comment '问卷优先级',
status string comment '状态',
enable_flag string comment '可用标识',
created_by string comment '创建人',
creation_date string comment '创建时间',
last_updated_by string comment '最后更新人',
last_update_date string comment '最后更新时间',
version_num string comment '版本号')
COMMENT '中国区NPS问卷内容配置表'
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
   'hdfs://hacluster/AppData/BIProd/ODS/PHONESERVICE/ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM'
数据文件推送路径：/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM
XML配置文件:
<FileToHDFS action="ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM/*_ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>PHONESERVICE/ODS_PHONESERVICE_NPS_CONTENT_DETAIL_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>