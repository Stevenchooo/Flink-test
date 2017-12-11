1,游戏券发放明细数据源:
CREATE EXTERNAL TABLE ODS_GAME_COUPON_DELIVER_DETAILS_DM (
campaignId bigint comment '活动ID',
userId string comment '华为账号ID',
appId string comment '游戏ID，用户充值的游戏',
spend string comment '现金消费金额',
amount string comment '赠送游戏券金额',
deliverTime string comment '游戏券发放时间',
useAppId string comment '定向券对应的游戏ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_COUPON_DELIVER_DETAILS_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_COUPON_DELIVER_DETAILS_DM   

XML配置文件：

           <FileToHDFS action="ODS_GAME_COUPON_DELIVER_DETAILS_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_COUPON_DELIVER_DETAILS_DM/*ODS_GAME_COUPON_DELIVER_DETAILS_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_COUPON_DELIVER_DETAILS_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_COUPON_DELIVER_DETAILS_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
2,活动详情页数据源:
CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM (	  
campaignId bigint comment '活动ID',
userId string comment '华为账号ID',
imei string comment '手机IMEI',
time string comment '访问活动页面时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM   

XML配置文件：

           <FileToHDFS action="ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM/*ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
3,活动参加数据源:
CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_JOIN_DM (
campaignId bigint comment '活动ID',
userId string comment '华为账号ID',
imei string comment '手机IMEI',
time string comment '参加活动时间',
awardIds string comment '奖品ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_JOIN_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_JOIN_DM   

XML配置文件：

           <FileToHDFS action="ODS_GAME_CAMPAIGN_JOIN_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_JOIN_DM/*ODS_GAME_CAMPAIGN_JOIN_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_CAMPAIGN_JOIN_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_JOIN_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
4,活动应用名单数据源:	
CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_APP_DM (  
campaignId bigint comment '活动ID',
type int comment '应用名单类型',
appId string comment '应用ID',
appListId int comment '动态应用名单ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_APP_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_APP_DM   

XML配置文件：

           <FileToHDFS action="ODS_GAME_CAMPAIGN_APP_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_APP_DM/*ODS_GAME_CAMPAIGN_JOIN_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_CAMPAIGN_APP_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_APP_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
