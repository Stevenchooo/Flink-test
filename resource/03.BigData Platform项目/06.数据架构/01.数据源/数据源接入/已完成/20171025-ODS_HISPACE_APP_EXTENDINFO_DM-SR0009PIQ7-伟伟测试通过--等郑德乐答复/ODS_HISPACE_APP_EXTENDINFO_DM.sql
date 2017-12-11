CREATE EXTERNAL TABLE ODS_HISPACE_APP_EXTENDINFO_DM(
appoid string comment 'id',
appkeywords string comment '应用关键字',
apptarifftype string comment '应用内资费类型',
toshelvetime string comment '预约上架时间',
isappforcedupdate string comment '是否强迫用户更新升级app应用',
type string comment '类型',
pegigradelevel string comment 'pegi分级级别',
uskgradelevel string comment 'usk分级级别',
acbgradelevel string comment 'acb分级级别',
iarcgradelevel string comment 'iarc分级级别',
esragradelevel string comment 'esra分级级别',
defaultlan string comment '默认语言',
isimmersed string comment '是否沉浸式显示')
PARTITIONED BY ( pt_d string  )
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
   'hdfs://hacluster/AppData/BIProd/ODS/HISPACE/ODS_HISPACE_APP_EXTENDINFO_DM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_APP_EXTENDINFO_DM
XML配置文件:
<FileToHDFS action="ODS_HISPACE_APP_EXTENDINFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_APP_EXTENDINFO_DM/*_ODS_HISPACE_APP_EXTENDINFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HISPACE_APP_EXTENDINFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HISPACE/ODS_HISPACE_APP_EXTENDINFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>