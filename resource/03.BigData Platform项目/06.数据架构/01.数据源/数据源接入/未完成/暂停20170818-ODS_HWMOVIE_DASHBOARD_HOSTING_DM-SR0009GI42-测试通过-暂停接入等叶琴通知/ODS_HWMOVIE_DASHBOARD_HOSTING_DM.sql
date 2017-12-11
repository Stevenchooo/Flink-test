CREATE EXTERNAL TABLE ODS_HWMOVIE_DASHBOARD_HOSTING_DM(
dtime string comment '统计的时间',
projectid string comment '项目id',
projectname string comment '项目名称',
region string comment '地区部',
country string comment '国家',
newdeivceactivate bigint comment '设备业务激活',
deivceactivate_d bigint comment '设备业务日活',
deivceactivate_m bigint comment '设备业务月活',
totaldeviceactivate bigint comment '总设备业务激活',
newregisteraccount bigint comment '新注册帐号',
registeractivate_d bigint comment '帐号日活',
registeractivate_m bigint comment '帐号月活',
totalregisteraccount bigint comment '总注册帐号',
totalactivate bigint comment '总活跃帐号',
newpayaccount bigint comment '新支付帐号',
payaccountactivate_d bigint comment '支付帐号日活',
payaccountactivate_m bigint comment '支付帐号月活',
totalpayaccount bigint comment '总支付帐号',
payamount bigint comment '消费流水',
devicearpu bigint comment '设备业务ARPU',
accountarpu bigint comment '帐号ARPU',
payaccountarpu bigint comment '支付帐号ARPU',
videos bigint comment '点播内容数量',
views bigint comment '播放总次数',
viewers bigint comment '播放总用户数',
totalviewminute bigint comment '播放总时长(分)',
avgviewminuteperviewer bigint comment '人均播放时长(分)',
avgviewsperviewer bigint comment '人均播放次数',
avgminuteperview bigint comment '次均播放时长(分)')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_DASHBOARD_HOSTING_DM' 

数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_DASHBOARD_HOSTING_DM 

XML配置文件：

           <FileToHDFS action="ODS_HWMOVIE_DASHBOARD_HOSTING_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_DASHBOARD_HOSTING_DM/*ODS_HWMOVIE_DASHBOARD_HOSTING_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWMOVIE_DASHBOARD_HOSTING_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_DASHBOARD_HOSTING_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>