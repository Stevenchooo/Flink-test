CREATE EXTERNAL TABLE ODS_GAME_COUPON_TRANS_INFO_DM (
zOrderId string comment '券订单号',
zid string comment 'Z券ID',
amount string comment '扣减金额',
useAppId string comment '联盟应用ID',
cpCostPercent string comment 'CP承担游戏券成本比例',
requestId string comment '外部订单号',
source string comment '券来源')
PARTITIONED BY (pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\u0001',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='u\0001')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_COUPON_TRANS_INFO_DM' 
   
   
推送数据文件路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_COUPON_TRANS_INFO_DM   

xml配置信息：

       <FileToHDFS action="ODS_GAME_COUPON_TRANS_INFO_DM">
        <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_COUPON_TRANS_INFO_DM/*_ODS_GAME_COUPON_TRANS_INFO_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>360</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_COUPON_TRANS_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_COUPON_TRANS_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
       </FileToHDFS>