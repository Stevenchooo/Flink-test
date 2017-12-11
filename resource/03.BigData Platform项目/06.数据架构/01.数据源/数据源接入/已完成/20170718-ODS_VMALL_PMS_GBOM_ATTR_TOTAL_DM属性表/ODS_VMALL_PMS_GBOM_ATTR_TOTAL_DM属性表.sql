属性表：
CREATE EXTERNAL TABLE ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM (
id bigint comment '主键id',
sbom_id bigint comment 'SBOM ID',
gbomCode String comment 'gbomCode',
product_id bigint comment '商品ID',
category_attr_id bigint comment '商品属性id,t_pms_attr_source.id',
prd_gbom_attr_name String comment '商品属性名称',
prd_atrr_val_id bigint comment '商品属性值ID',
prd_gbom_attr_val String comment '商品属性值',
create_date String comment '创建日期')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM' 
   
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM

XML配置文件：
           <FileToHDFS action="ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM/*ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VMALL/ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

	  
ODS_VMALL_PMS_SBOM_PRICE_DM
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_SBOM_PRICE_DM
数据文件推送名称：  *ODS_VMALL_PMS_SBOM_PRICE_DM_20170804000000.txt.lzo   （日期必须精确到秒）

ODS_VMALL_PMS_SBOM_DM
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_SBOM_DM
数据文件推送名称：  *ODS_VMALL_PMS_SBOM_DM_20170804000000.txt.lzo   （日期必须精确到秒）

ODS_VMALL_PMS_PRODUCT_DM
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_PRODUCT_DM
数据文件推送名称：  *ODS_VMALL_PMS_PRODUCT_DM_20170804000000.txt.lzo   （日期必须精确到秒）

ODS_VMALL_PMS_PRD_CATEGORY_DM
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_PRD_CATEGORY_DM
数据文件推送名称：  *ODS_VMALL_PMS_PRD_CATEGORY_DM_20170804000000.txt.lzo   （日期必须精确到秒）

ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM
数据文件推送名称：  *ODS_VMALL_PMS_GBOM_ATTR_TOTAL_DM_20170804000000.txt.lzo   （日期必须精确到秒）