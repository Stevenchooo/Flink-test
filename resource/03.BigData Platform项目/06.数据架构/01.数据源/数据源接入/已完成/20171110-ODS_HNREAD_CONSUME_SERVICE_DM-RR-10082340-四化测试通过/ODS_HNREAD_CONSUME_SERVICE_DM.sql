CREATE EXTERNAL TABLE ODS_HNREAD_CONSUME_SERVICE_DM(
statis_day string comment '数据日期',
buy_time string comment '购买时间',
operate_num string comment '操作流水',
qq_no string comment 'QQ号',
bookid string comment '书籍编号',
consume_price string comment '消费价格',
book_name string comment '书籍名',
pay_type string comment '购买方式',
chapter_id string comment '章节序号',
sub_platform string comment '子平台',
quan_cnt string comment '书券消耗',
pay_channel string comment '支付渠道')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HNREAD/ODS_HNREAD_CONSUME_SERVICE_DM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HNREAD_CONSUME_SERVICE_DM
XML配置文件:
<FileToHDFS action="ODS_HNREAD_CONSUME_SERVICE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HNREAD_CONSUME_SERVICE_DM/*ODS_HNREAD_CONSUME_SERVICE_DM*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HNREAD_CONSUME_SERVICE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HNREAD/ODS_HNREAD_CONSUME_SERVICE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>