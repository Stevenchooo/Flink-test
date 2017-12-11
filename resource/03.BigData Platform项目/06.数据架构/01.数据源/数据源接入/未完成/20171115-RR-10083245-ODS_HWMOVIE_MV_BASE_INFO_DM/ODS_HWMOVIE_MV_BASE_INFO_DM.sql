CREATE EXTERNAL TABLE ODS_HWMOVIE_MV_BASE_INFO_DM(
mvid string comment '聚合后的影片id',
defaultlanguage string comment '默认语言',
mvtype int comment '影片类型',
volumecount int comment '最大集数',
isfinish int comment '是否已完结',
updatevolume int comment '更新到第几集',
paytype int comment '初始资费类型',
price int comment '建议价格',
tvodtime string comment 'T片开始时间。',
svodtime string comment 'S片开始时间。',
freetime string comment '免费开始时间。',
categoryid string comment '一级分类',
themeid string comment '媒资的二级分类',
productioncountry string comment '内容出品的国家/地区',
releasedate string comment '内容发行日期',
audiolanguage string comment '视频的对白语言',
distributor string comment '发行商字典项编码列表',
state int comment '影片运营状态',
imdbscore string comment 'IMDB评分',
doubanscore string comment '豆瓣评分',
alias string comment '影片别名',
updatefrequency string comment '更新频率',
rating string comment '分级信息',
artistid string comment '影人信息',
definition string comment '清晰度',
duration string comment '片长',
applytype int comment '使用方式限制',
Tags string comment '标签')
PARTITIONED BY ( pt_d string )
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_MV_BASE_INFO_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_MV_BASE_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_MV_BASE_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_MV_BASE_INFO_DM/*_ODS_VIDEO_HIMOVIE_MV_BASE_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_MV_BASE_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_MV_BASE_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>