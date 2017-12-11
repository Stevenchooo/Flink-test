CREATE EXTERNAL TABLE ODS_PBI_TERMINAL_DEVICE_DM(
item string comment 'ITEM编码',
series_id string comment '小类代码',
series_name string comment '小类名称',
offering_id string comment 'OFFERING编号',
offering_code string comment 'OFFERING编码',
desc_cn string comment '中文描述',
desc_en string comment '英文描述',
version string comment '版本',
prod_supply_mode string comment '产品供应模式',
custom_property string comment '定制属性',
color_cn string comment '颜色',
color_en string comment '颜色',
brand string comment '品牌',
stype_cn string comment '款式说明',
stype_en string comment '款式说明',
attribute_cn string comment '功能描述',
attribute_en string comment '功能描述',
brief_name string comment '对外名称',
brief_en_name string comment '对外英文名称',
inside_name string comment '对内型号',
device_name string comment '对外型号',
limited_status string comment '受限状态',
lifecycle string comment '生命周期状态',
last_update_time string comment '更新时间',
icsl string comment 'ICSL',
net_channel string comment '制式频段',
country_code string comment '国家ID',
country_en_name string comment '国家',
carrier_cn string comment '运营商',
carrier_en string comment '运营商',
pbimktname_cn string comment 'Offering-传播名',
pbimktname_en string comment 'Offering-传播名',
high_low_config string comment '高低配版本',
high_low_config_en string comment '高低配版本',
model_cn string comment '制式',
model_en string comment '制式',
rom string comment 'RAM',
ram string comment 'ROM')
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
   'hdfs://hacluster/AppData/BIProd/ODS/PBI/ODS_PBI_TERMINAL_DEVICE_DM'
数据文件推送路径：/MFS/DataIn/CDM/odsdata/ODS_PBI_TERMINAL_DEVICE_DM
XML配置文件:
<FileToHDFS action="ODS_PBI_TERMINAL_DEVICE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/CDM/odsdata/ODS_PBI_TERMINAL_DEVICE_DM/*_ODS_PBI_TERMINAL_DEVICE_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_PBI_TERMINAL_DEVICE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>PBI/ODS_PBI_TERMINAL_DEVICE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>