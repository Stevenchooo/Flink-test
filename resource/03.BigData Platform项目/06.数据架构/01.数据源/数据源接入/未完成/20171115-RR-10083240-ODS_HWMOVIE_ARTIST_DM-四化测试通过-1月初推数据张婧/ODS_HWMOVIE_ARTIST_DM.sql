CREATE EXTERNAL TABLE ODS_HWMOVIE_ARTIST_DM(
artistid string comment '影人ID',
artisttype string comment '职责',
isteam int comment '是否团队组合',
firstname string comment '名',
lastname string comment '姓',
searchname string comment '艺术家检索名',
firstletter string comment '艺术家首字母',
sex int comment '艺术家性别',
zone string comment '艺术家归属地区',
companyname string comment '艺术家所属公司名称',
buildtime string comment '艺术家出道时间',
joinintime string comment '艺术家签约公司时间',
birthday string comment '生日格式',
education string comment '教育程度',
bloodgroup string comment '血型',
favorite string comment '爱好',
mothertongue string comment '母语',
height int comment '身高',
weight int comment '体重',
marriage int comment '婚姻状态',
stype string comment '艺术家风格',
keywords string comment '关键字',
defaultlanguage string comment '默认语言信息',
constellation string comment '星座',
nationality string comment '国籍')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_ARTIST_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_ARTIST_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_ARTIST_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_ARTIST_DM/*_ODS_HWMOVIE_ARTIST_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_ARTIST_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_ARTIST_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>