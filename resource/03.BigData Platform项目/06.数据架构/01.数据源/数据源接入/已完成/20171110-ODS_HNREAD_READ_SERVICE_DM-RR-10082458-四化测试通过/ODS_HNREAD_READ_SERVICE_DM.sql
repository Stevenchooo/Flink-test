CREATE EXTERNAL TABLE ODS_HNREAD_READ_SERVICE_DM(
statis_day string comment '数据日期',
statis_time string comment '数据时间',
user_id string comment '用户id',
download_code string comment '参数code',
book_id string comment '书籍id',
chapter_num string comment '章节id',
software_version string comment '版本号',
qq_num string comment '用户QQ',
column_id string comment '渠道号')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HNREAD/ODS_HNREAD_READ_SERVICE_DM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HNREAD_READ_SERVICE_DM
XML配置文件:
<FileToHDFS action="ODS_HNREAD_READ_SERVICE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HNREAD_READ_SERVICE_DM/*ODS_HNREAD_READ_SERVICE_DM*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HNREAD_READ_SERVICE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HNREAD/ODS_HNREAD_READ_SERVICE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>