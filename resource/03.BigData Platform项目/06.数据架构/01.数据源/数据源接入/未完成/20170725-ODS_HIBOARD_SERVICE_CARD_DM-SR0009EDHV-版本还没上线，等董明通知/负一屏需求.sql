服务信息
CREATE EXTERNAL TABLE ODS_HIBOARD_SERVICE_CARD_DM (
titleCH string comment '中文名字',
serviceName string comment '服务名字',
titleEn string comment '英文名字',
H5URL string comment 'web版url',
packageName string comment '支持此服务的app包名',
activityName string comment '服务的主Activity',
intent string comment '跳转需要的字段',
startMode string comment '0:默认app启动1:h5启动2:shortcut',
state string comment '服务状态',
category string comment '类别',
minVersionCode string comment '最小版本号',
maxVersionCode string comment '最大版本号')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIBOARD/ODS_HIBOARD_SERVICE_CARD_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HIBOARD_SERVICE_CARD_DM   

XML配置文件：

           <FileToHDFS action="ODS_HIBOARD_SERVICE_CARD_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HIBOARD_SERVICE_CARD_DM/*ODS_HIBOARD_SERVICE_CARD_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>0</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HIBOARD_SERVICE_CARD_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HIBOARD/ODS_HIBOARD_SERVICE_CARD_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

		  
新闻信息
CREATE EXTERNAL TABLE ODS_HIBOARD_NEWS_CARD_DM (
pubTime string comment '新闻发布时间',
template string comment '新闻模板类型',
cardId string comment '卡片唯一标识',
upTimes string comment '顶次数',
detailUrl string comment '新闻详情页url',
text String comment '正文字段',
replyCount string comment '跟帖数',
tag string comment '新闻标签列表',
digest string comment '新闻摘要',
type string comment '新闻类型',
imgUrl string comment '新闻图片url',
downTimes string comment '踩次数',
category string comment '新闻类别',
title string comment '卡片标题',
source string comment '新闻来源',
cpId string comment '新闻来源')
PARTITIONED BY (                                                       
   pt_d string,pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIBOARD/ODS_HIBOARD_NEWS_CARD_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HIBOARD_NEWS_CARD_DM   

XML配置文件：

           <FileToHDFS action="ODS_HIBOARD_NEWS_CARD_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HIBOARD_NEWS_CARD_DM/*ODS_HIBOARD_NEWS_CARD_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HIBOARD_NEWS_CARD_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HIBOARD/ODS_HIBOARD_NEWS_CARD_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>