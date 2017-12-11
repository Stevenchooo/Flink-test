CREATE EXTERNAL TABLE ODS_BISDK_WEB_DM (
typeId string comment '类型ID',
link string comment '点击(或搜索)的内容',
revenue string comment '收益(未使用)',
idsite string comment 'ID',
hour string comment '客户端时间(小时)',
min string comment '客户端时间(分钟)',
sec string comment '客户端时间(秒)',
url string comment '被访问页面url',
urlref string comment '上一级url(未使用)',
refpath string comment '(未使用)',
id string comment 'Cookie(唯一的16位长度16进制字符串)',
idts string comment 'Cookie创建时间',
idvc string comment '访问次数',
idn string comment '新访问者',
rcn string comment '未使用',
rck string comment '未使用',
refts string comment '渠道来源时间',
viewts string comment '最后一次访问时间',
scd string comment '屏幕颜色深度',
vpr string comment '窗口分辨率',
ects string comment '上次订单时间(未使用)',
ref string comment '渠道来源url(未使用)',
cvar_pg string comment '用户自定义page数据',
java string comment 'java插件支持',
pdf string comment 'pdf插件支持',
qt string comment 'quicktime插件支持',
realp string comment 'realaudio插件支持',
wma string comment 'wma插件支持',
dir string comment 'director插件支持',
fla string comment 'flash插件支持',
gears string comment 'gears插件支持',
ag string comment 'silverlight插件支持',
res string comment '分辨率',
cookie string comment '是否支持cookie',
data string comment '用户自定义数据',
cvar_vi string comment '用户自定义visit数据(未使用)',
serverts string comment 'serverts',
ip string comment 'ip',
os string comment 'os',
browser string comment '浏览器',
version string comment '版本',
language string comment '语言',
sid string comment 'sid',
ua string comment 'ua')
PARTITIONED BY (                                                       
   pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_WEB_DM' 

   数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_WEB_DM   

XML配置文件：

           <FileToHDFS action="ODS_BISDK_WEB_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_WEB_DM/*ODS_BISDK_WEB_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_BISDK_WEB_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>BISDK/ODS_BISDK_WEB_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
