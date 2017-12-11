CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_VOD_INFO_DM(
vodid string comment '内容ID',
contentcode string comment '内容CODE （索引）',
vodtype string comment 'VOD类型',
contenttype string comment '内容类型',
name_lang1 string comment '内容名称',
name_lang2 string comment '内容名称',
name_lang3 string comment '内容名称',
launchdate string comment '上线时间',
expiredate string comment '下线时间',
genre1 string comment 'VOD类别',
genre2 string comment 'VOD类别',
genreid string comment '',
spid string comment '内容提供商编号',
spname string comment '内容提供商名称',
areacode string comment '区域ID',
areaname string comment '区域名称',
createtime string comment '创建时间',
commercialtime string comment '内容商用时间',
producedate string comment '出品日期描述',
supplylang string comment 'VOD能支持的语言描述',
companyid string comment '发行公司id',
subtitlelang string comment 'VOD能支持的对白语种',
companyname1 string comment '公司名称',
companyname2 string comment '公司名称',
companyname3 string comment '公司名称',
sdpcontenttype string comment 'sdp上内容类型',
updatetime string comment '操作时间',
billingid string comment '内容提供商assertID',
showtype string comment '流派',
product string comment 'ADI接口中对应的Product',
launchdateutc string comment '上线时间UTC',
expiredateutc string comment '下线时间UTC',
createtimeutc string comment '创建时间UTC',
commercialtimeutc string comment '内容商用时间UTC',
updatetimeutc string comment '更新时间UTC',
vodprice string comment 'VOD价格',
posterpath string comment '海报位置',
operatorid string comment '创建VOD的操作员ID',
actiontype string comment '操作类型',
foreignsn string comment 'VOD外接的CMS的内容编号',
vodnum string comment 'VOD集数',
definitionFlag string comment '内容清晰度标识')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_VOD_INFO_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_VOD_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_VOD_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_VOD_INFO_DM/*_ODS_HIMOVIE_HVS_REL_VOD_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_VOD_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_VOD_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>