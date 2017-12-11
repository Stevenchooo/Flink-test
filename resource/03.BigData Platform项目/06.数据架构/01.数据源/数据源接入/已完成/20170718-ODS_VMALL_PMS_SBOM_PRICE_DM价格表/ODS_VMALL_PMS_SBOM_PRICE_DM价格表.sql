价格表：
CREATE EXTERNAL TABLE ODS_VMALL_PMS_SBOM_PRICE_DM (
id bigint comment 'SBOM价格ID',
sbom_id bigint comment 'SBOM id',
sbom_code String comment 'SBOM编码',
purchase_price string comment '采购价',
price string comment '商品价格',
operate_date String comment '最后一次操作日期',
operator String comment '最后一次操作人',
sync_status bigint comment '同步状态',
effective String comment '生效时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_SBOM_PRICE_DM' 
   
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_SBOM_PRICE_DM

XML配置文件：
           <FileToHDFS action="ODS_VMALL_PMS_SBOM_PRICE_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_SBOM_PRICE_DM/*ODS_VMALL_PMS_SBOM_PRICE_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VMALL_PMS_SBOM_PRICE_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VMALL/ODS_VMALL_PMS_SBOM_PRICE_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
