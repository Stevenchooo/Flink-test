CREATE EXTERNAL TABLE  ODS_PSI_IP_ADDRESS_DM (
ip_id string comment '主键',
ip_start string comment '起始ip地址',
ip_end string comment '终止ip地址',
ip_start_decimalism string comment '起始ip地址十进制',
ip_end_decimalism string comment '终止ip地址十进制',
country_a2 string comment '国家二位编码',
country_number string comment '国家数字编码',
province string comment '省',
city string comment '城市',
county string comment '区县',
match_level string comment '匹配等级',
created_by string comment '创建人',
creation_date string comment '创建时间',
last_updated_by string comment '修改人',
last_update_date string comment '修改时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/PSI/ODS_PSI_IP_ADDRESS_DM' 

数据文件推送路径：/MFS/DataIn/TCSM/odsdata/ODS_PSI_IP_ADDRESS_DM

XML配置文件：

           <FileToHDFS action="ODS_PSI_IP_ADDRESS_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/TCSM/odsdata/ODS_PSI_IP_ADDRESS_DM/*ODS_PSI_IP_ADDRESS_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\t</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_PSI_IP_ADDRESS_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>PSI/ODS_PSI_IP_ADDRESS_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
