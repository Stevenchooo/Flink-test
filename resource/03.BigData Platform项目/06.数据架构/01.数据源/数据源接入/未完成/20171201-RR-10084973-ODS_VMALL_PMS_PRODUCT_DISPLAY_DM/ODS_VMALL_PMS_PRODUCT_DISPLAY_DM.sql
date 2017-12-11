CREATE EXTERNAL TABLE ODS_VMALL_PMS_PRODUCT_DISPLAY_DM(
id string comment '展示商品ID',
code string comment '展示商品编码',
publish_id string comment '发布ID',
product_id string comment '商品ID',
carrier_id string comment '运营商ID',
channel_id string comment '渠道ID',
name string comment '展示商品名称',
brief_name string comment '展示商品简称',
status int comment '展示商品状态',
weight string comment '商品重量',
prom_word string comment '促销语',
prom_word_link string comment '促销语链接地址',
create_by string comment '创建人',
create_time string comment '创建时间',
update_by string comment '修改人',
update_time string comment '修改时间',
sync_status int comment '同步状态',
is_virtual int comment '是否为虚拟商品',
product_type string comment '商品类别',
sequence string comment '序列',
limited_quantity string comment '限购数量',
cps_category_id string comment 'CPS分类ID',
on_shelf_time string comment '上架时间',
model string comment '规格型号',
invoice_provider string comment '发票供应商',
is_invoice int comment '是否开票',
hw_pc_url string comment '华为PC侧跳转链接',
hw_wap_url string comment '华为WAP侧跳转链接',
hw_app_url string comment '华为APP侧跳转链接',
SEOTitle string comment 'SEO标题',
SEOKeyword string comment 'SEO关键字',
note string comment '备注',
is_search int comment '是否可被搜索',
detail_style int comment '详情样式',
sec_channel_id string comment '二级渠道ID')
COMMENT '展示商品表'
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_PRODUCT_DISPLAY_DM'
数据文件推送路径：/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_PRODUCT_DISPLAY_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_PMS_PRODUCT_DISPLAY_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_PRODUCT_DISPLAY_DM/*_ODS_VMALL_PMS_PRODUCT_DISPLAY_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_PMS_PRODUCT_DISPLAY_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_PMS_PRODUCT_DISPLAY_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>