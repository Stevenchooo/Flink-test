CREATE EXTERNAL TABLE ODS_MUSIC_DASHBOARD_HOSTING_DM (
date String comment '日期',
projectid String  comment  '项目id',         
projectname String comment '项目名称',        
totalincome bigint comment  '总收入', 
contentincome bigint comment '内容收入',    
revenuepackage bigint comment  '会员收入',
totaluser bigint comment  '音乐用户数',
freeuser bigint comment '免费会员数',
unfreeuser bigint comment '收费会员数',
graceuser bigint comment '宽限期会员数',
clubpurchasenum  bigint comment '会员扣费次数',
contentcount  bigint  comment '内容购买次数',
packagenewsuccess  bigint  comment '新增会员次数',
packagerenewalchange bigint comment '变更会员次数',
packageoptout  bigint comment '退订会员次数',
userlogin  bigint comment '登录用户数',
uniquevisit bigint comment 'UV',
monthuv bigint comment 'UV(30days)',
streamtimes  bigint comment '试听次数',
loginstreamtimes  bigint comment '试听次数(登陆后)',
streamingusers  bigint comment '试听用户数',
avgstreamingtimes String comment '平均试听全曲数',
avgstreaminglen  String comment '平均试听时长(min)')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'=',',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'=',')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/MUSIC/ODS_MUSIC_DASHBOARD_HOSTING_DM' 

   数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_DASHBOARD_HOSTING_DM   

XML配置文件：

           <FileToHDFS action="ODS_MUSIC_DASHBOARD_HOSTING_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_DASHBOARD_HOSTING_DM/*ODS_MUSIC_DASHBOARD_HOSTING_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>,</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_MUSIC_DASHBOARD_HOSTING_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>MUSIC/ODS_MUSIC_DASHBOARD_HOSTING_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  

CREATE EXTERNAL TABLE ODS_MUSIC_USERACTIVITY_HOSTING_DM (
dtime String comment '统计的时间',
projectid  String  comment  '项目id',         
projectname String comment '项目名称',        
equipmentnewusersapp bigint comment  'APP新增设备数', 
equipmentnewusersportal bigint comment 'Portal新增设备数',    
equipmenttotalusersapp bigint comment  'APP累计设备数',
equipmenttotalusersportal bigint comment  'Portal累计设备数',
dequipmentuvweb bigint comment '基于UVAID排重，且渠道为WEB的设备数',
dequipmentuvwap bigint comment '基于UVAID排重，且渠道为WAP的设备数',
dequipmentuvapp bigint comment '基于UVAID排重，且渠道为APP的设备数',
dequipmentuvussd  bigint comment '基于UVAID排重，且渠道为USSD的设备数',
mequipmentuvweb  bigint  comment '最近30天的数据，基于UVAID排重，且渠道为WEB的设备数',
mequipmentuvwap  bigint  comment '最近30天的数据，基于UVAID排重，且渠道为WAP的设备数',
mequipmentuvapp bigint comment '最近30天的数据，基于UVAID排重，且渠道为APP的设备数',
mequipmentuvussd  bigint comment '最近30天的数据，基于UVAID排重，且渠道为USSD的设备数',
equipmentuvpercent  String comment 'DAU占比',
accountnewusers bigint comment '新增用户数',
accounttotalusers bigint comment '累计用户数',
daccountuvweb  bigint comment '基于account排重，且渠道为WEB的用户数',
daccountuvwap  bigint comment '基于account排重，且渠道为WAP的用户数',
daccountuvapp  bigint comment '基于account排重，且渠道为APP的用户数',
daccountuvussd bigint comment '基于account排重，且渠道为USSD的用户数',
maccountuvweb  bigint comment '最近30天的数据，基于accountD排重，且渠道为WEB的用户数',
maccountuvwap bigint comment  '最近30天的数据，基于accountD排重，且渠道为WAP的用户数', 
maccountuvapp bigint comment '最近30天的数据，基于accountD排重，且渠道为APP的用户数',    
maccountuvussd bigint comment  '最近30天的数据，基于accountD排重，且渠道为USSD的用户数',
accountuvpercent String comment  'DAU占比')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'=',',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'=',')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/MUSIC/ODS_MUSIC_USERACTIVITY_HOSTING_DM' 

   数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_USERACTIVITY_HOSTING_DM   

XML配置文件：

           <FileToHDFS action="ODS_MUSIC_USERACTIVITY_HOSTING_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_USERACTIVITY_HOSTING_DM/*ODS_MUSIC_USERACTIVITY_HOSTING_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>,</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_MUSIC_USERACTIVITY_HOSTING_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>MUSIC/ODS_MUSIC_USERACTIVITY_HOSTING_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>