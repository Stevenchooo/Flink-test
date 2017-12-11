CREATE EXTERNAL TABLE ODS_HISPACE_APP_COMMENT_INFO_DM(
appid string comment '应用appid',
accountid string comment '用户华为账号ID',
commentinfo string comment '评论内容',
opertime string comment '发表评论时间',
rating int comment '评分',
versionname string comment '评论用户安装应用的版本',
phonetype string comment '评论用户的机型')
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='\u0001', 
   'line.delim'='\n',
   'serialization.format'='\u0001')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/HISPACE/ODS_HISPACE_APP_COMMENT_INFO_DM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_APP_COMMENT_INFO_DM
XML配置文件:
<FileToHDFS action="ODS_HISPACE_APP_COMMENT_INFO_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_APP_COMMENT_INFO_DM/*_ODS_HISPACE_APP_COMMENT_INFO_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HISPACE_APP_COMMENT_INFO_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HISPACE/ODS_HISPACE_APP_COMMENT_INFO_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>