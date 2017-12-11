CREATE EXTERNAL TABLE ODS_VMALL_PMS_GBOM_DM(
gbom_code string comment 'GBOM编码',
product_id string comment '产品ID',
gbom_name string comment 'GBOM名称',
brief_name string comment '简称',
standard string comment '制式',
running_memory string comment '运行内存',
fuselage_memory string comment '机身内存',
first_color string comment '一级颜色',
second_color string comment '二级颜色',
has_battery string comment '是否有电池',
battery_model string comment '电池型号',
battery_type string comment '是否报备',
magneticable string comment '是否有磁性',
category_id string comment 'GBOM所属分类',
create_by string comment '创建人',
create_time string comment '创建时间',
update_by vstring comment '修改人',
update_time string comment '修改时间',
sync_status string comment '同步状态',
status string comment 'gbom状态',
attr_concat string comment 'gbom属性集合',
is_integration string comment '是否机电一体',
gbom_Type string comment 'gbom类型',
isValidGbom string comment '是否有效真实gbom',
product_type string comment '商品型号',
soft_name string comment '软件名称',
tax_code string comment '税务编码')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_GBOM_DM'
数据文件推送路径：/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_GBOM_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_PMS_GBOM_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_GBOM_DM/*_ODS_VMALL_PMS_GBOM_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_PMS_GBOM_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_PMS_GBOM_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>