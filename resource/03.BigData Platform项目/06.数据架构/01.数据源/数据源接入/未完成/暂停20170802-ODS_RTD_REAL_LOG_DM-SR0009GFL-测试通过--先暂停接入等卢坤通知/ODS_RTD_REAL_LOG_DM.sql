CREATE EXTERNAL TABLE ODS_RTD_REAL_LOG_DM (
user_id string comment '消费者编号', 
oper_type int comment '操作类型', 
account_type int comment '账号类型',
user_account string comment '用户账号',
device_type int comment '设备类型',
device_id string comment '设备id',
req_client_ip string comment 'ip',
req_time string comment '请求时间',
rule_id string comment '规则id',
risk_level int comment '风险级别',
risk_flag string comment '风险标记',
terminaltype string comment '终端类型',
deviceid2 string comment '设备id2',
uuiddigest string comment 'uuid',
frequentlyused int comment '常用标记',
fullytrusteddev int comment '可信标记',
twostepverify int comment '两步验证是否开启',
twosteptime string comment '两步验证时间',
twostepflag string comment '两步验证标记',
version string comment '版本号',
reqclienttype int comment '客户端类型',
flowid string comment '流水号',
logintype int comment '登录类型',
appid string comment 'appid',
weakpwdflag int comment '弱密码',
userstate int comment '用户状态',
loginchannel int comment '渠道号',
active_days int comment '账号活跃天数',
ip_location_days int comment '帐号在请求IP段上的活跃天数',
cur_prov_days int comment '帐号在请求IP归属地',
max_prov string comment '帐号归属地',
max_prov_days int comment '帐号归属地',
device_active_days int comment '帐号在请求设备上的天数',
phone_locate_prov 帐号如果是手机号码，（精确到省份id）	string comment '手机号码归属地',
user_login_ips int comment '帐号登录的IP数',
ip_locate_prov string comment 'IP归属地',
ip_login_users int comment 'IP上请求的帐号量',
ip_login_succ_users int comment 'IP上请求成功帐号量',
ip_max_provs int comment 'IP上帐号的最大省份代码量',
device_login_users int comment '设备上请求帐号量',
device_login_succ_users int comment '设备上请求成功帐号量',
user_state int comment '帐号状态',
device_state int comment '设备状态',
ip_state int comment 'IP状态')
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
   'hdfs://hacluster/AppData/BIProd/ODS/RTD/ODS_RTD_REAL_LOG_DM' 
   
数据文件推送路径：   /MFS/DataIn/hadoop-NJ/odsdata/ODS_RTD_REAL_LOG_DM

XML配置文件：
           <FileToHDFS action="ODS_RTD_REAL_LOG_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_RTD_REAL_LOG_DM/*ODS_RTD_REAL_LOG_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_RTD_REAL_LOG_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>RTD/ODS_RTD_REAL_LOG_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

