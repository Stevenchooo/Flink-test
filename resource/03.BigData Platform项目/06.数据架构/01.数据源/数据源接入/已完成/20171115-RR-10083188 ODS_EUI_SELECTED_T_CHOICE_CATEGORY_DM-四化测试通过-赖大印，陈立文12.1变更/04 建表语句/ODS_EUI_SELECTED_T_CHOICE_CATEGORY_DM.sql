CREATE EXTERNAL TABLE ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM(
id string comment '主键id',
chname string comment '中文名称')
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
   'hdfs://hacluster/AppData/BIProd/ODS/EUI/ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM'
数据文件推送路径：/MFS/DataIn/EUI-BI/odsdata/ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM
XML配置文件:
<FileToHDFS action="ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/EUI-BI/odsdata/ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM/*_ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>EUI/ODS_EUI_SELECTED_T_CHOICE_CATEGORY_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>