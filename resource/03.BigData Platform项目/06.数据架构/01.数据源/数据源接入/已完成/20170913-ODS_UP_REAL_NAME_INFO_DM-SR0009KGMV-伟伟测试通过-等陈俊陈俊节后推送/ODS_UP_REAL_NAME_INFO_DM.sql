CREATE EXTERNAL TABLE ODS_UP_REAL_NAME_INFO_DM (
userID string comment '主键',
realName string comment '实名',
ctfType string comment '证件类型',
ctfCode string comment '证件号码',
bankCVerifyFlag int comment '支付绑卡验证标识',
bankCVerifyChannel string comment '支付绑卡认证渠道',
idVerifyFlag string comment '身份证验证标识',
idVerifyChannel string comment '身份证验证渠道',
ctfPicURL1 string comment '证件照片URL1',
ctfPicURL2 string comment '证件照片URL2',
ctfPicURL3 string comment '证件照片URL3',
updateTime string comment '数据更新时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/UP/ODS_UP_REAL_NAME_INFO_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_REAL_NAME_INFO_DM

XML配置文件：

           <FileToHDFS action="ODS_UP_REAL_NAME_INFO_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_REAL_NAME_INFO_DM/*ODS_UP_REAL_NAME_INFO_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_UP_REAL_NAME_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>UP/ODS_UP_REAL_NAME_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
