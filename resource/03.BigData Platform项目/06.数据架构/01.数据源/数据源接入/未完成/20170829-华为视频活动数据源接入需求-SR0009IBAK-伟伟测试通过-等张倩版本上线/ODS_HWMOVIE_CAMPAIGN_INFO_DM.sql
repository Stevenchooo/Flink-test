CREATE EXTERNAL TABLE ODS_HWMOVIE_CAMPAIGN_INFO_DM(
campid String comment '活动ID',
campsubject String comment '活动主题',
camptype int comment '活动类型',
campexpiredate String comment '活动有效期',
camppriority int comment '活动优先级',
campdesc String comment '活动描述',
campstatus int comment '活动状态',
detailurl String comment 'URL',
campalias String comment '活动别名',
thirdpartyurl String comment '第三方路径',
packagename String comment '活动上传的ZIP包名',
appid bigint comment '活动渠道标识',
maxgroup bigint comment '活动可成的最大团数',
groupsize bigint comment '每个团的人数',
groupluckysize bigint comment '每个团的中奖数',
beid String comment '活动渠道标识默认中国',
maxjointimes String comment '活动最大参与次数')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_CAMPAIGN_INFO_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_CAMPAIGN_INFO_DM 

XML配置文件：

           <FileToHDFS action="ODS_HWMOVIE_CAMPAIGN_INFO_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_CAMPAIGN_INFO_DM/*ODS_HWMOVIE_CAMPAIGN_INFO_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWMOVIE_CAMPAIGN_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_CAMPAIGN_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
