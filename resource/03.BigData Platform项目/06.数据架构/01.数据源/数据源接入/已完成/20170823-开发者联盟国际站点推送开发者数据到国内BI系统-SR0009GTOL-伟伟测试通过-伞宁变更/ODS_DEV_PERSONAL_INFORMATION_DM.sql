CREATE EXTERNAL TABLE ODS_DEV_PERSONAL_INFORMATION_DM(
userID string comment '用户ID(内部)',
ctfType bigint comment '证据类型(实名认证项)',
ctfCode string comment '证件号码(加密存储)(实名认证项)',
realName string comment '开发者真实姓名(实名认证项)(AES128加密)',
province string comment '开发者所在省份(实名认证项)',
city string comment '开发者在城市(实名认证项)',
attachment1 string comment '附件1(身份证正面照片)(文件加密存储)(实名认证项)',
attachment2 string comment '附件2(身份证反面照片)(文件加密存储)(实名认证项)',
attachment3 string comment '附件3',
lastUpdateTime string comment '开发者信息更新时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_PERSONAL_INFORMATION_DM' 

数据文件推送路径：/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_PERSONAL_INFORMATION_DM 

XML配置文件：

           <FileToHDFS action="ODS_DEV_PERSONAL_INFORMATION_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_PERSONAL_INFORMATION_DM/*ODS_DEV_PERSONAL_INFORMATION_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_DEV_PERSONAL_INFORMATION_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>DEV/ODS_DEV_PERSONAL_INFORMATION_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
