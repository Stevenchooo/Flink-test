CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM(
epgid string comment 'EPG服务器ID',
accesstime string comment '访问时间',
accesstype string comment '访问类型',
accessresult string comment '访问结果',
subscriberid string comment '用户的唯一标识',
physicaldeviceid string comment '物理设备标识',
domain string comment '领域',
profileid string comment 'ProfileID',
terminaltype string comment '终端型号',
terminalvendor string comment '设备厂商型号',
epgip string comment '终端访问服务器的地址',
terminalip string comment '终端IP地址',
stbversion string comment '机顶盒软件版本号',
drmtype string comment '标识该设备支持的DRM类型',
returncode string comment 'UE的认证返回码',
loginname string comment '登录用户名',
sessionid string comment '用户登录的会话ID',
terminalosversion string comment 'Forthnet局点预留字段',
useragent string comment 'Forthnet局点预留字段',
virturedeviceid string comment '逻辑设备ID',
accesstimeutc string comment '访问时间',
subscribersn string comment '订户内部键值',
profilesn string comment '用户内部键值')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM/*_ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_EPG_ACCESS_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>