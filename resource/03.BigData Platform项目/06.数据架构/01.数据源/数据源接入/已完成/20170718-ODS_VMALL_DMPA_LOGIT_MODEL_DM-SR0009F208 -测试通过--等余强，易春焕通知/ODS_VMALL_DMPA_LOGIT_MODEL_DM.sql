CREATE EXTERNAL TABLE ODS_VMALL_DMPA_LOGIT_MODEL_DM (
user_id String comment '用户ID',
u_id String comment '设备ID',
ip String comment 'IP指用户浏览vmall所使用ip',
plat String comment '用户当天访问所使用的平台 ',
presult decimal(20,1) comment '用户逻辑回归模型值',
l_date String comment '日期分区')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_DMPA_LOGIT_MODEL_DM' 
   
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_DMPA_LOGIT_MODEL_DM

XML配置文件：
           <FileToHDFS action="ODS_VMALL_DMPA_LOGIT_MODEL_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_DMPA_LOGIT_MODEL_DM/*ODS_VMALL_DMPA_LOGIT_MODEL_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VMALL_DMPA_LOGIT_MODEL_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VMALL/ODS_VMALL_DMPA_LOGIT_MODEL_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
