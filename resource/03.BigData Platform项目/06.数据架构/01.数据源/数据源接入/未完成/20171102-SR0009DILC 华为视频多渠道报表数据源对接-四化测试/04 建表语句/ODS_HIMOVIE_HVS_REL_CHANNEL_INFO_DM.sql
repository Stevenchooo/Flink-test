CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM(
contentid string comment '频道编号',
contentcode string comment '外系统主键',
name_lang1 string comment '频道名称1',
name_lang2 string comment '频道名称2',
name_lang3 string comment '频道名称3',
status string comment '频道状态',
contenttype string comment '频道类别',
spid string comment '节目提供商代码',
spname string comment '提供商名称',
areacode string comment '区域CODE',
areaname string comment '区域名称',
genre1 string comment '流派1',
genre2 string comment '流派2',
genreid string comment '流派ID信息',
livestatus string comment '频道状态',
tvodstatus string comment '频道状态',
pltvstatus string comment '频道状态',
starttime string comment '开始时间',
endtime string comment '结束时间',
starttimeutc string comment '开始的UTC时间',
endtimeutc string comment '结束的UTC时间',
isdelete string comment '是否已经删除',
createtime string comment '创建时间',
updatetime string comment '更新时间',
createtimeutc string comment '创建时间UTC时间',
updatetimeutc string comment '更新时间的UTC时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM/*_ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_CHANNEL_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>