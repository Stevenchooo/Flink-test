CREATE EXTERNAL TABLE ODS_GAME_USER_OPRATION_CPS_DM (
dtstatdate string comment '日期',
gamename string comment '游戏名称',
gamepkg string comment '游戏包名',
idayact string comment '日活跃用户数',
idaynew string comment '日新增用户数',
i2dayrest string comment '新增次留率',
i3dayrest string comment '新增三日留存率',
i7dayrest string comment '新增七日留存率',
i14dayrest string comment '新增十四日留存率',
i30dayrest string comment '新增三十日留存率',
idaynewpay string comment '新增付费用户数',
idaynewpaymoney string comment '新增付费总额',
idaypay string comment '日付费用户数',
idaypaymoney string comment '日付费总额',
arpu string comment '日付费总额/日活跃用户数',
arppu string comment '日付费总额/日付费用户数',
ltv2 string comment '当天新增用户且在注册日及第2天的充值金额/日新增用户数',
ltv3 string comment '当天新增用户且在注册日及后面2天总共3天内的充值金额/日新增用户数',
ltv7 string comment '当天新增用户且在注册日及后面6天总共7天内的充值金额/日新增用户数',
ltv14 string comment '当天新增用户且在注册日及后面13天总共14天内的充值金额/日新增用户数',
ltv30 string comment '当天新增用户且在注册日及后面29天总共30天内的充值金额/日新增用户数',
wlostrate string comment '周流失率',
wbackrate string comment '周回流率',
mlostrate string comment '月流失率',
mbackrate string comment '月回流率')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_USER_OPRATION_CPS_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_USER_OPRATION_CPS_DM   

XML配置文件：

           <FileToHDFS action="ODS_GAME_USER_OPRATION_CPS_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_USER_OPRATION_CPS_DM/*ODS_GAME_USER_OPRATION_CPS_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_GAME_USER_OPRATION_CPS_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>GAME/ODS_GAME_USER_OPRATION_CPS_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
