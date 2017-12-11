CREATE EXTERNAL TABLE ODS_UP_DL_SEND_MESSAGE_DM (
timeStamp String comment '生成该条话单记录的时间戳',
transactionId String comment '会话id。mc生成的会话id',
destAddr String comment '被叫号码',
countryCode String comment '手机号的国家码',
carrier String comment '国内手机号对应的运营商。如：CMCC，CUCC，CTCC',
orgAddr String comment '主叫号码，即接入号。',
recvTime String comment 'mc接收请求时间',
sendTime String comment '发送短信到短信网关的时间',
channel String comment '运营商通道标识',
msgID String comment '短信序列号',
serviceID String comment '发送短信的业务标识（appId）',
result String comment '取值如下：0 等待回执1 回执成功2 回执失败3 发送失败4 其他错误',
respresult String comment '短信网关返回结果',
contentLength int comment '短信内容长度',
delay int comment '下发消息时延，单位ms',
taskId String comment '业务下发短信关联的任务标识')
PARTITIONED BY (pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\u0001',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='u\0001')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/UP/ODS_UP_DL_SEND_MESSAGE_DM' 
   
   
推送数据文件路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_DL_SEND_MESSAGE_DM   

xml配置信息：

       <FileToHDFS action="ODS_UP_DL_SEND_MESSAGE_DM">
        <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_DL_SEND_MESSAGE_DM/*ODS_UP_DL_SEND_MESSAGE_DM_*.log</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>360</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_UP_DL_SEND_MESSAGE_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>UP/ODS_UP_DL_SEND_MESSAGE_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
       </FileToHDFS>
