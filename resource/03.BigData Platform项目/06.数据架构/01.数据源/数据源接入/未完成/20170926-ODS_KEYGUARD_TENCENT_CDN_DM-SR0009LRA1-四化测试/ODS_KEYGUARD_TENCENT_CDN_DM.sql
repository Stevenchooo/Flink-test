CREATE EXTERNAL TABLE ODS_KEYGUARD_TENCENT_CDN_DM (
Request_time string comment '请求时间',
Client_IP string comment '访问域名的客户端IP',
domain_name string comment '被访问域名',
file_path string comment '文件请求路径',
bytes_number string comment '本次访问字节数大小',
province string comment '省份',
Operator string comment '运营商',
Return_code string comment '返回码',
referer string comment '参考信息',
Request_duration string comment '请求时长(毫秒)',
User_agent string comment '用户代理',
range string comment '文件范围',
Method string comment '请求方法',
Protocol string comment '协议标识',
Cache string comment '缓存命中情况',
CDN_VIP string comment 'CDN本节点VIP')
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
   'hdfs://hacluster/AppData/BIProd/ODS/KEYGUARD/ODS_KEYGUARD_TENCENT_CDN_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_KEYGUARD_TENCENT_CDN_DM

XML配置文件：

           <FileToHDFS action="ODS_KEYGUARD_TENCENT_CDN_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_KEYGUARD_TENCENT_CDN_DM/*ODS_KEYGUARD_TENCENT_CDN_DM*.txt.gz</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_KEYGUARD_TENCENT_CDN_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>KEYGUARD/ODS_KEYGUARD_TENCENT_CDN_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
