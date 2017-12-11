CREATE EXTERNAL TABLE ODS_VMALL2_TBL_SYS_REGION_DM(
id STRING comment '主键ID',
parent_id STRING comment '该地址的父地址ID',
code STRING comment '编码',
name STRING comment '地址名称',
status STRING comment '状态',
parent_number_code STRING comment '该地址的父地址及父父地址连接串',
create_date STRING comment '创建日期',
post_code STRING comment '邮政编码')
PARTITIONED BY (                                                       
   pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL2_TBL_SYS_REGION_DM' 
   
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL2_TBL_SYS_REGION_DM

XML配置文件：
           <FileToHDFS action="ODS_VMALL2_TBL_SYS_REGION_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL2_TBL_SYS_REGION_DM/*ODS_VMALL2_TBL_SYS_REGION_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VMALL2_TBL_SYS_REGION_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VMALL/ODS_VMALL2_TBL_SYS_REGION_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>







