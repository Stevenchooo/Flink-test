CREATE EXTERNAL TABLE  ODS_PSI_ACT_ANTI_FRAUD_DM (
item_code string comment 'BOM编码',
INSIDE string comment '华为内部机型名称',
EXTERNAL string comment '外部机型名称',
software_version string comment 'Android版本信息',
emui_version string comment '预装的EMUI版本编号',
exception_flag string comment '异常开机的监控',
anti_fraud_flag string comment '激活反欺诈监控')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\t',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='\t')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/PSI/ODS_PSI_ACT_ANTI_FRAUD_DM' 

数据文件推送路径：/MFS/DataIn/TCSM/odsdata/ODS_PSI_ACT_ANTI_FRAUD_DM

XML配置文件：

           <FileToHDFS action="ODS_PSI_ACT_ANTI_FRAUD_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/TCSM/odsdata/ODS_PSI_ACT_ANTI_FRAUD_DM/*ODS_PSI_ACT_ANTI_FRAUD_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\t</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_PSI_ACT_ANTI_FRAUD_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>PSI/ODS_PSI_ACT_ANTI_FRAUD_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>