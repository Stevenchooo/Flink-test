CREATE EXTERNAL TABLE ODS_PHONESERVICE_NPS_QUERY_LOG_DM(
id string comment '主键',
insert_time string comment '时间',
sn string comment '加密方式为终端数据底座通用加密服务',
imei string comment '加密方式为终端数据底座通用加密服务',
sn_sha256 string comment 'SHA256加密SN',
imei_sha256 string comment 'SHA256加密IMEI',
activated_time string comment '激活时间',
channel string comment '手机服务出货类型',
app_id string comment '手机服务分配给客户端的应用标识',
model string comment '客户端机型',
emui_version string comment 'emui版本号',
android_version string comment '操作系统版本',
country_code string comment '国家编码',
client_local string comment '客户端语种编码',
times string comment '问卷次数',
firmware string comment '客户端Rom版本号',
content_id string comment '问卷内容标识id',
l_date string comment '分区')
COMMENT '中国区NPS问卷访问日志表'
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
   'hdfs://hacluster/AppData/BIProd/ODS/PHONESERVICE/ODS_PHONESERVICE_NPS_QUERY_LOG_DM'
数据文件推送路径：/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_QUERY_LOG_DM
XML配置文件:
<FileToHDFS action="ODS_PHONESERVICE_NPS_QUERY_LOG_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/phoneservice/odsdata/ODS_PHONESERVICE_NPS_QUERY_LOG_DM/*_ODS_PHONESERVICE_NPS_QUERY_LOG_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_PHONESERVICE_NPS_QUERY_LOG_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>PHONESERVICE/ODS_PHONESERVICE_NPS_QUERY_LOG_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>