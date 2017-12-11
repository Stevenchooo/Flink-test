CREATE EXTERNAL TABLE ODS_WALLET_WA_POI_DM (
id bigint,
name string,
description string,
longitude string,
latitude string,
createTime string,
updateTime string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_WA_POI_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_WA_POI_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_WA_POI_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_WA_POI_DM/*_ODS_WALLET_WA_POI_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_WA_POI_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_WA_POI_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
		  
	
	
ODS_WALLET_T_WS_EVENT_CARDENROLL_DM
新增字段：
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (channel string comment '开卡渠道');  
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (appPackageName string comment '应用包名');
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (imei string comment '加密后的imei号，请解密后使用');
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (clientVersion string comment '客户端版本号');


ODS_WALLET_EVENT_CARDSWING_DM
新增字段：
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (transTerminal string comment '终端机编号');  
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (transType string comment '交易类型');
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (poiId string comment '加密后的兴趣点id，需要解密使用');
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (imei string comment '加密后的imei号，需要解密使用');
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (clientVersion string comment '客户端版本号');

