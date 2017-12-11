CREATE EXTERNAL TABLE ODS_PHONESERVICE_NPS_BUS_TYPE_DM(
id string comment '主键',
nps_id string comment '业务ID',
nps_title string comment '问卷业务',
qty string comment '问卷次数',
keep_time string comment '数据保留时长',
auto_flag string comment '是否自动激活',
status string comment '状态',
intervals string comment '问卷间隔',
black_list string comment '黑名单',
match_method string comment '语言匹配方式',
sn_list string comment '测试设备SN列表',
enable_flag string comment '可用标识',
created_by string comment '创建人',
creation_date string comment '创建时间',
last_updated_by string comment '最后更新人',
last_update_date string comment '最后更新时间',
version_num string comment '版本号')
PARTITIONED BY ( pt_d string )
COMMENT '中国区NPS问卷业务类型表'
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
   'hdfs://hacluster/AppData/BIProd/ODS/PHONESERVICE/ODS_PHONESERVICE_NPS_BUS_TYPE_DM'
数据文件推送路径：/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_BUS_TYPE_DM
XML配置文件:
<FileToHDFS action="ODS_PHONESERVICE_NPS_BUS_TYPE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_BUS_TYPE_DM/*_ODS_PHONESERVICE_NPS_BUS_TYPE_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_PHONESERVICE_NPS_BUS_TYPE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>PHONESERVICE/ODS_PHONESERVICE_NPS_BUS_TYPE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>