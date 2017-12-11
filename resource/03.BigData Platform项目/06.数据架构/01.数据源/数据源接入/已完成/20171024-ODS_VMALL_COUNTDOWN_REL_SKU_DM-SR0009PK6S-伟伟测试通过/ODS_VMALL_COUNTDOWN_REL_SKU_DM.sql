CREATE EXTERNAL TABLE ODS_VMALL_COUNTDOWN_REL_SKU_DM(
id string comment '主键ID',
countdown_id string comment '限时抢购活动ID',
sku_id string comment 'SkuId',
countdown_price string comment '抢购价',
count string comment '数量',
countdown_code string comment '限时抢购活动CODE',
sbom_code string comment 'SBOM CODE',
promotion_word string comment '促销语')
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='\u0001', 
   'line.delim'='\n',
   'serialization.format'='\u0001')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_COUNTDOWN_REL_SKU_DM'
数据文件推送路径：/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_COUNTDOWN_REL_SKU_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_COUNTDOWN_REL_SKU_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_COUNTDOWN_REL_SKU_DM/*_ODS_VMALL_COUNTDOWN_REL_SKU_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_COUNTDOWN_REL_SKU_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_COUNTDOWN_REL_SKU_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>