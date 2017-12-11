CREATE EXTERNAL TABLE ODS_HWMOVIE_MV_BASE_INFO_BE_DM(
mvid string comment '聚合后的影片id',
beid string comment '渠道ID',
scoreflag int comment '评分标识',
optscore string comment '人工评分',
displayscore string comment '实际显示的评分值',
admirecount string comment '点赞次数',
collectcount string comment '收藏次数',
recommendcount string comment '推荐次数',
optplaycount string comment '人工干预点播次数',
sysplaycount string comment '系统统计点播次数',
cornertagids string comment '角标id列表',
status int comment '状态',
auditstatus int comment '审核状态',
offlinereason int comment '下架原因',
offlineremark string comment '下架原因备注',
templateid string comment '关联模板ID',
postermodified int comment '海报是否被编辑过',
stillmodified int comment '剧照是否被编辑过',
bgmodified int comment '背景图是否被编辑过',
scheduledonlinetime string comment '预约上架时间',
actualonlinetime string comment '正式上架时间',
cornermodified int comment '角标是否被编辑过',
stationtagid int comment '角标是否被编辑过')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_MV_BASE_INFO_BE_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_MV_BASE_INFO_BE_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_MV_BASE_INFO_BE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_MV_BASE_INFO_BE_DM/*_ODS_HWMOVIE_MV_BASE_INFO_BE_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_MV_BASE_INFO_BE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_MV_BASE_INFO_BE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>