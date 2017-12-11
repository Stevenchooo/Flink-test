CREATE EXTERNAL TABLE ODS_HWMOVIE_PLAY_RECORD_DM (
D_TIME string comment '数据时间',
DEVICEID string comment '设备ID，作为终端的唯一标识',
DEVICETYPE string comment '设备类型',
MACADDRESS string comment 'MAC地址',
SUBSCRIBERID string comment '订户ID',
DEVICEMODEL string comment '设备型号，例如：MR303A, iphone4s',
DEVICESUPPLIER string comment '设备供应商，例如：Huawei',
DEVICESOC string comment '设备芯片型号，例如：BCM 7405',
IPADDRESS string comment 'IP地址，该IP是终端上报的，可能是内网IP',
DEVICEIP string comment '置空',
SUBNETMASK string comment '子网掩码',
DEFAULTGATEWAY string comment '默认网关',
CLIENTVERSION string comment '客户端版本号',
DEVICEOS string comment '设备操作系统',
PLAYER string comment '播放器类型和版本号',
ACCESSTYPE string comment '接入类型：Ethernet, WiFi, 3G, LTE',
NTPSERVER string comment 'NTP服务器的IP地址',
DNSSERVER string comment 'DNS服务器的IP地址',
CONTENTID string comment '播放的内容ID',
URL string comment '节目的URL',
STREAMTYPE Int comment '置空',
SERVERIP string comment '如果是组播填组播IP，如果是单播填HMS IP',
SERVICETYPE string comment '业务类型 LiveTV, VOD, Recording, IR, NVOD.',
RESULT Int comment '0：没播出来1：播放成功2：播放失败3：播放中断4：播放结束5：缓冲放弃',
STARTTIME string comment '播放开始时间',
ENDTIME string comment '播放结束时间',
PLAYOUTDELAY Int comment '播放时延，单位：毫秒。',
PLAYDURATION Int comment '播放时长',
PLAYDURATIONHISTOGRAM string comment '每个码率的节目播放的时长',
PROFILESWITCHDOWN Int comment '码率下切（码率减小）的次数',
PROFILESWITCHUP Int comment '码率上切（码率增大）的次数',
DOWNLOADSPEED Int comment '平均下载速率',
DOWNLOADSPEEDHISTOGRAM string comment '每个分片计算一个下载速率',
STALLINGCOUNT Int comment '卡顿次数',
STALLINGDURATION Int comment '卡顿时长，单位：毫秒',
STALLINGHISTOGRAM string comment '卡顿时长直方图',
MOSHISTOGRAM string comment 'MOS在各个区间的秒数',
MOSAVG Float comment 'MOS平均值',
TERMINAL_TYPE string comment '设备类型',
EMUIVERSION string comment 'EMUI版本号',
IMEI string comment '手机/Pad的IMEI号',
ERRORDESC string comment '播放失败原因',
EPGIP string comment '播放前需到EPG鉴权',
EPGRESULT Int comment 'EPG鉴权结果',
EPGDELAY Int comment '从用户点播该节目到UI调用播放器之前的所有时延，如果没收到EPG响应',
STARTUPEVENT string comment '起播播放事件日志',
VIDEOQUALITYSETBYUSER string comment '用户选择的清晰度',
PROFILEEVENT string comment '码率切换事件',
REDIRECTTIMES Int comment '播放请求重定向次数',
ACCESSDELAY Int comment '接入时延',
INITBUFFERDELAY Int comment '初缓时延',
STALLINGTRACK string comment '【本节目的】卡顿记录',
VIDEOQUALITY string comment '观看内容的最高分辨率')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_PLAY_RECORD_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_PLAY_RECORD_DM 

XML配置文件：

           <FileToHDFS action="ODS_HWMOVIE_PLAY_RECORD_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_PLAY_RECORD_DM/*_ODS_HWMOVIE_PLAY_RECORD_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWMOVIE_PLAY_RECORD_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_PLAY_RECORD_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>