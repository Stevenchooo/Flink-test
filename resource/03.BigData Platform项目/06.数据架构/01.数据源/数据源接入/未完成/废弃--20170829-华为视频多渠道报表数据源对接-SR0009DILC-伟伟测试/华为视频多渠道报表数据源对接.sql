ODS_VIDEO_HIMOVIE_CP_BASE_INFO_DM  
新增字段：
alter table ODS_VIDEO_HIMOVIE_CP_BASE_INFO_DM  add columns (hvsSubNetId string comment 'HVS的子网ID');  

ODS_HWMOVIE_PROMOTION_CDR_DM
新增字段：
alter table ODS_HWMOVIE_PROMOTION_CDR_DM  add columns (beId string comment '运营主体ID');  
alter table ODS_HWMOVIE_PROMOTION_CDR_DM  add columns (timeZone string comment '时区信息');  

ODS_HWMOVIE_EPG_ACCESS_STAT_DM
新增字段：
alter table ODS_HWMOVIE_EPG_ACCESS_STAT_DM  add columns (timeZone  string comment '时区信息');  

ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM
新增字段：
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM  add columns (timeZone  string comment '时区信息');  

ODS_HWMOVIE_REL_PLAY_RECORD_VMOS_DM
新增字段：
alter table ODS_HWMOVIE_REL_PLAY_RECORD_VMOS_DM  add columns (hvsSubNetId string comment 'HVS的子网ID'); 
alter table ODS_HWMOVIE_REL_PLAY_RECORD_VMOS_DM  add columns (timeZone  string comment '时区信息');  

ODS_HWMOVIE_SUBSCRIBER_RENTINFO_DM
新增字段：
alter table ODS_HWMOVIE_SUBSCRIBER_RENTINFO_DM  add columns (hvsSubNetId string comment 'HVS的子网ID'); 
alter table ODS_HWMOVIE_SUBSCRIBER_RENTINFO_DM  add columns (timeZone  string comment '时区信息');  

ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM
新增字段：
alter table ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM  add columns (beId string comment '默认为华为视频中国区运营渠道标识'); 
alter table ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM  add columns (timeStampUTC  string comment '时间');  

新增表
ODS_VIDEO_CLOUD_APPID_INFO_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_APPID_INFO_DM(
appId String comment '视频云APPID',
brandId String comment 'APP所属的品牌信息',
appNameZH String comment 'APP中文名称',
appNameEN String comment 'APP英文名称',
appType	String comment 'APP的类型',
packageName String comment '包名',
minVersionCode String comment '最低版本',
maxVersionCode String comment '最高版本',
appSignType int comment '应用标识')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_APPID_INFO_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_INFO_DM 

XML配置文件：

           <FileToHDFS action="ODS_VIDEO_CLOUD_APPID_INFO_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_INFO_DM/*ODS_VIDEO_CLOUD_APPID_INFO_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_APPID_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_APPID_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  

新增表
ODS_VIDEO_CLOUD_APPID_MAP_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_APPID_MAP_DM(
appId String comment '视频云分配的appid主键',
upAppId String comment '账号分配的appid',
payAppId String comment '支付分配的appid')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_APPID_MAP_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_MAP_DM 

XML配置文件：

           <FileToHDFS action="ODS_VIDEO_CLOUD_APPID_MAP_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_MAP_DM/*ODS_VIDEO_CLOUD_APPID_MAP_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_APPID_MAP_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_APPID_MAP_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

	  
新增表	  
ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM(
beId String comment '运营主体ID',
contoryCode String comment '二位字母代码',
countryCallingCode String comment '最低版本',
isDefaultCountry int comment '是否默认国家',
countryNameZH String comment '国家中文名',
countryNameEN String comment '国家英文名',
defaultLanguage String comment '国家标准语言',
language String comment '支持的语言',
timeZone String comment '国家标准时区',
currencyCode String comment '国家标准币种',
fractionalCurrency String comment '国家标准辅币名称')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM 

XML配置文件：

           <FileToHDFS action="ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM/*ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
新增表	  
ODS_VIDEO_CLOUD_BE_INFO_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_BE_INFO_DM(	  
beId String comment '渠道标识',
beNameZH String comment '渠道中文名称',
beNameEN String comment '渠道英文名称',
brandId String comment '所属品牌',
beType int comment '渠道类型',
status int comment '状态',
siteId String comment '区域站点ID',
defaultCountry String comment '国家标识',
appType String comment '运营渠道的终端类型',
hvsSubNetId String comment '渠道对应的HVS的子网标识',
hvsAreaId String comment '渠道对应的HVS的区域标识',
hvsBossId String comment '渠道对应的HVS的BossId')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_BE_INFO_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_INFO_DM 

XML配置文件：

           <FileToHDFS action="ODS_VIDEO_CLOUD_BE_INFO_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_INFO_DM/*ODS_VIDEO_CLOUD_BE_INFO_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_BE_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_BE_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
