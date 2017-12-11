CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM (
id bigint,
orderid string,
logtime string,
description string,
ordertype bigint,
appid bigint,
userid string,
useridhash bigint,
encdeviceid string,
deviceid string,
status bigint,
originalprice bigint,
price bigint,
finalprice bigint,
producttype bigint,
classid bigint,
contentid string,
volumeid string,
productname string,
promotion string,
voucher string,
rfdorderid string,
rfdprice bigint,
rfdreason string,
refundtime string,
createtime string,
updatetime string,
hwpayid string,
paytype bigint,
paytime string,
completetime string,
extra string,
extend string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM'    
   
数据文件推送路径：   /MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM/ 


  XML配置信息：
           <FileToHDFS action="ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM/*_ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_ORDER_INFO_LOG_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

