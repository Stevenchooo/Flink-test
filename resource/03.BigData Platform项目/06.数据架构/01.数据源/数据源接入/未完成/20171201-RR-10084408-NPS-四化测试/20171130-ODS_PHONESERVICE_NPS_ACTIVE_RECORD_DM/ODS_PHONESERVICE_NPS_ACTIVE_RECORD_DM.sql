CREATE EXTERNAL TABLE ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM(
id string comment '主键',
sn_sha256 string comment 'SHA256加密SN',
nps_id string comment '问卷类型业务ID',
activated_time string comment '激活时间',
qty string comment '问卷成功查询次数',
first_query_time string comment '数据插入时间',
enable_flag string comment '可用标识',
created_by string comment '创建人',
creation_date string comment '创建时间',
version_num string comment '客户端版本号',
last_update_date string comment '更新时间',
l_date string comment '分区')
COMMENT '中国区NPS问卷激活记录表'
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
   'hdfs://hacluster/AppData/BIProd/ODS/PHONESERVICE/ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM'
数据文件推送路径：/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM
XML配置文件:
<FileToHDFS action="ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM/*_ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>PHONESERVICE/ODS_PHONESERVICE_NPS_ACTIVE_RECORD_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>