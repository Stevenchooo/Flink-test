1, ODS_WALLET_APPLICATION_DETAIL_DM
CREATE EXTERNAL TABLE ODS_WALLET_APPLICATION_DETAIL_DM (
aplictDetailID int comment '申请明细ID',
seq int comment '序号',
applicationID string comment '申请单号',
promotionID string comment '活动ID',
cardExpiredTime string comment '花币卡有效期',
cardDenomination int comment '花币卡面额',
reqQty int comment '花币卡申请数量',
inboundQty int comment '花币卡已入库数量',
outboundQty int comment '花币卡已出库数量',
receivedQty int comment '花币卡已确认收卡数量',
status int comment '状态',
makeCardTime string comment '制卡时间',
cardPublishAreaID string comment '发行地区ID',
cardPublishAreaName string comment '发行地区名称',
inBoundDate string comment '花币卡入库时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_APPLICATION_DETAIL_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_APPLICATION_DETAIL_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_APPLICATION_DETAIL_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_APPLICATION_DETAIL_DM/*_ODS_WALLET_APPLICATION_DETAIL_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_APPLICATION_DETAIL_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_APPLICATION_DETAIL_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
2, ODS_WALLET_APPLICATION_HEAD_DM
CREATE EXTERNAL TABLE ODS_WALLET_APPLICATION_HEAD_DM (
applicationID string comment '申请单号',
cardPublisher int comment '花币卡发行方',
applicationUses int comment '申卡用途',
applicationReason string comment '申卡原因',
status int comment '申请单状态',
creatorName string comment '申请人姓名',
createTime string comment '创建时间',
lastUpdateTime string comment '最后更新时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_APPLICATION_HEAD_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_APPLICATION_HEAD_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_APPLICATION_HEAD_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_APPLICATION_HEAD_DM/*_ODS_WALLET_APPLICATION_HEAD_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_APPLICATION_HEAD_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_APPLICATION_HEAD_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
3, ODS_WALLET_BUSI_DATA_DM
CREATE EXTERNAL TABLE ODS_WALLET_BUSI_DATA_DM (
itemID int comment '数据项ID',
itemName string comment '数据项名称',
itemDesc string comment '数据项描述',
segment_1 string comment '数据1',
segment_2 string comment '数据2',
segment_3 string comment '数据3',
segment_4 string comment '数据4',
segment_5 string comment '数据5',
segment_6 string comment '数据6',
segment_7 string comment '数据7',
segment_8 string comment '数据8',
createTime string comment '创建时间',
creator int comment '创建人ID',
lastUpdateTime string comment '最后更新时间',
lastUpdateBy int comment '最后更新人ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_BUSI_DATA_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_BUSI_DATA_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_BUSI_DATA_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_BUSI_DATA_DM/*_ODS_WALLET_BUSI_DATA_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_BUSI_DATA_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_BUSI_DATA_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
4, ODS_WALLET_CHARGE_ORDER_RCS_DM
CREATE EXTERNAL TABLE ODS_WALLET_CHARGE_ORDER_RCS_DM (
transactionID string comment '充值流水号',
userID string comment '用户UID',
orderNo string comment '订单号',
amount int comment '充值金额',
consumeState int comment '充值状态',
consumeMsg string comment '充值信息',
consumeTime string comment '消费时间',
consumeDetail string comment '消费明细')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_CHARGE_ORDER_RCS_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_CHARGE_ORDER_RCS_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_CHARGE_ORDER_RCS_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_CHARGE_ORDER_RCS_DM/*_ODS_WALLET_CHARGE_ORDER_RCS_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_CHARGE_ORDER_RCS_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_CHARGE_ORDER_RCS_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	   
	   
	   
	   
5, ODS_WALLET_POMOTION_INFO_DM
CREATE EXTERNAL TABLE ODS_WALLET_POMOTION_INFO_DM (
promotionID string comment '活动ID',
promotionName string comment '活动名称',
promotionDesc string comment '活动描述',
capitalQuota int comment '活动资金配额',
startDate string comment '活动开始时间',
endDate string comment '活动开始时间',
createTime string comment '创建时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_POMOTION_INFO_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_POMOTION_INFO_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_POMOTION_INFO_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_POMOTION_INFO_DM/*_ODS_WALLET_POMOTION_INFO_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_POMOTION_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_POMOTION_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
6, ODS_WALLET_RECHARGE_CARD_RCA_DM
CREATE EXTERNAL TABLE ODS_WALLET_RECHARGE_CARD_RCA_DM (
cardNo string comment '卡号',
applicationID string comment '申请单号',
aplictDetailID int comment '申请明细ID',
status int comment '卡状态',
activeTime string comment '激活时间',
destroyTime string comment '报废时间',
createTime string comment '创建时间',
lastUpdateTime string comment '最后更新时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_RECHARGE_CARD_RCA_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_RECHARGE_CARD_RCA_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_RECHARGE_CARD_RCA_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_RECHARGE_CARD_RCA_DM/*_ODS_WALLET_RECHARGE_CARD_RCA_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_RECHARGE_CARD_RCA_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_RECHARGE_CARD_RCA_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
7, ODS_WALLET_RECHARGE_CARD_RCS_DM
CREATE EXTERNAL TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM (
cardNo string comment '卡号',
faceValue int comment '卡面额',
validBal int comment '可用余额',
status int comment '卡状态',
issueTime string comment '发布时间',
expireTime string comment '过期时间',
consumeTime string comment '消费时间',
lastUpdateTime string comment '最后更新时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_RECHARGE_CARD_RCS_DM' 
   
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_RECHARGE_CARD_RCS_DM   

XML配置文件：

           <FileToHDFS action="ODS_WALLET_RECHARGE_CARD_RCS_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_RECHARGE_CARD_RCS_DM/*_ODS_WALLET_RECHARGE_CARD_RCS_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_WALLET_RECHARGE_CARD_RCS_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>WALLET/ODS_WALLET_RECHARGE_CARD_RCS_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
