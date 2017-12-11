CREATE EXTERNAL TABLE ODS_KEYGUARD_VERIZON_CDN_DM (
timestamp string,
time_taken string,
c_ip string,
filesize string,
s_ip string,
s_port string,
sc_status string,
sc_bytes string,
cs_method string,
cs_uri_stem string,
Encry_ption string,
rs_duration string,
rs_bytes string,
c_referrer string,
c_user_agent string,
customer_id string,
x_ec_custom string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/KEYGUARD/ODS_KEYGUARD_VERIZON_CDN_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_KEYGUARD_VERIZON_CDN_DM

XML配置文件：

           <FileToHDFS action="ODS_KEYGUARD_VERIZON_CDN_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_KEYGUARD_VERIZON_CDN_DM/*ODS_KEYGUARD_VERIZON_CDN_DM*.txt.gz</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_KEYGUARD_VERIZON_CDN_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>KEYGUARD/ODS_KEYGUARD_VERIZON_CDN_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

