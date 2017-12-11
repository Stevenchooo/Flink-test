CREATE EXTERNAL TABLE ODS_VMALL_PMS_BUNDLE_ITEMS_DM(
bundle_id string comment '捆绑套餐ID',
sbom_id string comment '商品销售ID',
quantity string comment '捆绑数量',
is_show int comment '是否在本sbom展示',
discount_amount string comment '折扣金额')
COMMENT '套餐明细表'
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='\0001', 
   'line.delim'='\n',
   'serialization.format'='\0001')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_BUNDLE_ITEMS_DM'
数据文件推送路径：/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_BUNDLE_ITEMS_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_PMS_BUNDLE_ITEMS_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_BUNDLE_ITEMS_DM/*_ODS_VMALL_PMS_BUNDLE_ITEMS_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_PMS_BUNDLE_ITEMS_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_PMS_BUNDLE_ITEMS_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>