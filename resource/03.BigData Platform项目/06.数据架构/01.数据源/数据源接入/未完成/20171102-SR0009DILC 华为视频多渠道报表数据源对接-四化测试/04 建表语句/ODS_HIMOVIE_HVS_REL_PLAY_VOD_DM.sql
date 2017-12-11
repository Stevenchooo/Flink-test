CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM(
user_name string comment '用户名称',
epgid string comment 'EPG服务器ID',
userid string comment '用户编号',
area_id string comment '用户区域ID',
area_name string comment '用户区域名称',
contentcode string comment '内容CODE',
contentid string comment '内容ID',
contentname1 string comment '内容名称1',
contentname2 string comment '内容名称2',
starttime string comment '业务开始时间',
endtime string comment '业务结束时间',
disconnectreason string comment '下线原因',
serverip string comment '服务ip',
timelength string comment '观看时长，单位秒',
url string comment '用户点播的URL',
productid string comment '产品ID',
productname1 string comment '产品名称1',
productname2 string comment '产品名称2',
domain string comment '领域',
clientip string comment '客户端ip',
service_mode string comment '操作类型',
user identity string comment '用户profileid',
virturedeviceid string comment '虚拟设备ID',
devicetype string comment '设备类型',
subnetid string comment '子网ID',
deviceid string comment '物理设备ID',
spid string comment 'SP编号',
spname string comment 'SP名称',
subjectcode string comment '外系统主键',
genre string comment '分类名称',
subject_id string comment '栏目ID',
groupid string comment '设备组id',
groupname string comment '设备组名称',
d_time string comment '统计时间',
visttime string comment '观看次数',
mediacode string comment '媒资CODE',
vodaction string comment 'VOD播放行为',
vodurl string comment 'VOD的链接',
seriesid string comment '连续剧ID',
programcode string comment 'IR的节目单CODE',
operate_type string comment '操作类型',
movienum string comment 'VOD的媒资ID',
starttimeutc string comment '话单的开始时间UTC',
endtimeutc string comment '话单的结束时间UTC',
connectiontype string comment '连接类型',
objectid string comment '内容的内部键值，全局唯一',
contenttype string comment '内容类型',
subscribersn string comment '订户的内部键值',
profilesn string comment '用户的内部键值',
genreid string comment '流派ID，多个以逗号分隔',
deviceoriginal string comment 'VSP原始的物理设备ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM/*_ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_PLAY_VOD_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>