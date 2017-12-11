CREATE EXTERNAL TABLE ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM (
logdate string comment '打点时间',
thirdPartyId string comment '三方在开发者联盟上申请的appId',      
userId string comment '用户uid',
interfaceName string comment '接口名称',
resultCode int comment '接口处理返回码')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HOMECLOUD/ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM' 

数据文件推送路径：/MFS/DataIn/homecloud/odsdata/ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM

XML配置文件：

           <FileToHDFS action="ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/homecloud/odsdata/ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM/*ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HOMECLOUD/ODS_HOMECLOUD_HEALTH_SERVICE_LOG_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
