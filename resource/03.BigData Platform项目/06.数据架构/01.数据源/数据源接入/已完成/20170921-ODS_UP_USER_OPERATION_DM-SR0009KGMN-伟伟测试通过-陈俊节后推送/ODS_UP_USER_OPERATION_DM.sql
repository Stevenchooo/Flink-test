CREATE EXTERNAL TABLE  ODS_UP_USER_OPERATION_DM (
userID string comment '用户ID(内部)',
operType int comment '操作类型',
serverName string comment '服务名',
reqClientType int comment '请求类型',
interFaceName string comment '接口名',
accountType int comment '账号类型',
userAccount string comment '用户类型',
deviceType int comment '设备类型',
deviceID string comment '设备id',
otherParams string comment '其他关键参数',
reqClientIp string comment '请求地址',
reqTime string comment '请求时间',
operTime string comment '返回时间',
successFlag string comment '成功标志',
errorCode string comment '错误码',
errorDesc string comment '错误描述',
loginChannel string comment '渠道号',
curSecretID string comment '当前密匙ID',
totalTime string comment '操作耗时(毫秒数)')
PARTITIONED BY (                                                       
   pt_d string,pt_h string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/UP/ODS_UP_USER_OPERATION_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_USER_OPERATION_DM

XML配置文件：

           <FileToHDFS action="ODS_UP_USER_OPERATION_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_USER_OPERATION_DM/*ODS_UP_USER_OPERATION_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_UP_USER_OPERATION_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>UP/ODS_UP_USER_OPERATION_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

