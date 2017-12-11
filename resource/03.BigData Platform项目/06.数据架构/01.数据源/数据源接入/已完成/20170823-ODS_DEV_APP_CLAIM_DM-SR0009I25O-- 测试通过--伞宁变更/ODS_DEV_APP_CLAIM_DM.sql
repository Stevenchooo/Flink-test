CREATE EXTERNAL TABLE ODS_DEV_APP_CLAIM_DM(
applyUserID string comment '申请应用认领的开发者userID',
applyUserName string comment '申请应用认领的开发者名字',
appId bigint comment '应用ID',
packageName string comment '应用包名',
appliedUserID string comment '被申请应用归属开发者的userID',
appliedUserName string comment '被申请应用归属开发者的名字',
appName string comment '应用名称',
applyTime string comment '申请时间',
auditor string comment '审批管理员用户账号',
auditTime string comment '审核时间',
auditStatus int	comment '审核结果')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\u0001',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='\u0001')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_APP_CLAIM_DM' 

数据文件推送路径：/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_APP_CLAIM_DM 

XML配置文件：

           <FileToHDFS action="ODS_DEV_APP_CLAIM_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_APP_CLAIM_DM/*ODS_DEV_APP_CLAIM_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_DEV_APP_CLAIM_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>DEV/ODS_DEV_APP_CLAIM_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
