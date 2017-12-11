商品表：
CREATE EXTERNAL TABLE ODS_VMALL_PMS_PRODUCT_DM (
id bigint comment '商品ID',
category_id bigint comment '商品所属分类',
code String comment '商品编码',
product_type bigint comment '商品类别 ',
is_virtual String comment '是否为虚拟商品',
name String comment '商品名称',
brief_name String comment '商品简称',
status String comment '商品状态',
create_by String comment '创建人',
create_time String comment '创建时间',
update_by String comment '修改人',
update_time String comment '修改时间',
is_consignment bigint comment '是否代销',
brand_id bigint comment '品牌id',
is_invoice String comment '是否开票',
invoice_provid bigint comment '发票供应商')
PARTITIONED BY (                                                       
   pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_PRODUCT_DM' 
   
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_PRODUCT_DM

XML配置文件：
           <FileToHDFS action="ODS_VMALL_PMS_PRODUCT_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_PRODUCT_DM/*ODS_VMALL_PMS_PRODUCT_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VMALL_PMS_PRODUCT_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VMALL/ODS_VMALL_PMS_PRODUCT_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
