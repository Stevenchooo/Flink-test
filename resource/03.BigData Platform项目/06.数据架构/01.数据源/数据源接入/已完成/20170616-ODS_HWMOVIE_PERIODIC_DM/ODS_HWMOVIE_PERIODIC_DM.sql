CREATE EXTERNAL TABLE ODS_HWMOVIE_PERIODIC_DM (
D_TIME string comment '数据时间',
StartTime string comment '本统计周期开始时间',
appStatCycle int comment '统计周期',
SequenceNumber int comment 'Report log的序列号',
DeviceID string comment '设备ID，作为终端的唯一标识',
DeviceType string comment '设备类型',
MACAddress string comment 'MAC地址',
SubscriberID string comment '订户ID',
DeviceModel string comment '设备型号',
DeviceSupplier string comment '设备供应商',
DeviceSoC string comment '设备芯片型号',
IPAddress string comment 'IP地址',
DeviceIP string comment '置空',
SubnetMask string comment '子网掩码',
DefaultGateway string comment '默认网关',
ClientVersion string comment '客户端版本号',
DeviceOS string comment '设备操作系统',
Player string comment '播放器类型和版本号',
EMUIVersion string comment 'EMUI版本号',
IMEI string comment '手机/Pad的IMEI号',
AccessType string comment '接入类型',
NTPServer string comment 'NTP服务器的IP地址',
DNSServer string comment 'DNS服务器的IP地址',
SignalStrengthHistogram string comment '信号强度直方图',
CPUUsageHistogram string comment 'CPU使用率直方图',
RAMUsageHistogram string comment '内存使用率直方图',
DownloadSpeedTrack string comment '下载速率采样',
BufferTrack string comment '缓冲区时长采样',
EDSDelay string comment 'APP调用EDS的重定向的时延',
LoginDelay string comment 'APP调用Login接口的时延',
AuthDelay string comment 'APP调用Authenticate接口的时延',
TVGuideDelay string comment 'APP调用获取频道列表的接口的时延',
VODListDelay string comment 'APP调用获取VOD节目列表的接口的时延',
VODDetailDelay string comment 'APP调用获取节目详情的接口的时延',
ErrorCode string comment 'APP调用EPG接口的错误码列表')
PARTITIONED BY (                                                       
   pt_d string comment '天分区')                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_PERIODIC_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_PERIODIC_DM 

XML配置文件：

           <FileToHDFS action="ODS_HWMOVIE_PERIODIC_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_PERIODIC_DM/*_ODS_HWMOVIE_PERIODIC_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWMOVIE_PERIODIC_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_PERIODIC_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>