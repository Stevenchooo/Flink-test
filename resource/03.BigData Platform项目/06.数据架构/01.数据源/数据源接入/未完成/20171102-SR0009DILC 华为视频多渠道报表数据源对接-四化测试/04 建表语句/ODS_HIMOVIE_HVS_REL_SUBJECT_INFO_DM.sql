CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM(
subjectid string comment '栏目ID',
subjectcode string comment '栏目IDCODE',
bizdomains string comment '所属领域',
name_lang1 string comment '内容名称',
name_lang2 string comment '内容名称',
name_lang3 string comment '内容名称',
subjecttype string comment '栏目类型',
launchdate string comment '上线时间',
expiredate string comment '下线时间',
subjectstatus string comment '栏目状态',
areacode string comment '区域Code',
areaname string comment '区域名称',
contenttype string comment '内容类型',
subnetid string comment '子网运营商ID',
parentid string comment '父栏目编码',
launchdateutc string comment '上线时间UTC',
expiredateutc string comment '下线时间UTC',
isdelete string comment '是否已经删除')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM/*_ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_SUBJECT_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>