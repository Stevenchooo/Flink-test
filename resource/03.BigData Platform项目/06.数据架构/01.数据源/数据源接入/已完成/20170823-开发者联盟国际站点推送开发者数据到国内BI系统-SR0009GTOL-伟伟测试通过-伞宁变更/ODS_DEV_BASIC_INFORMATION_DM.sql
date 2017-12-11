CREATE EXTERNAL TABLE ODS_DEV_BASIC_INFORMATION_DM(
userID string comment '用户ID（内部）',
userType bigint comment '用户类型',
developerStatus bigint comment '开发者有效状态',
registerAcct string comment '注册帐号',
registerDevlpTime string comment '注册为开发者时间',
userState bigint comment '用户激活状态',
realName string comment '实名',
verifyRealState bigint comment '(首次)实名认证审核状态',
firstAuditRealUID string comment '首次实名认证审批管理员用户ID',
firstAuditRealTime string comment '首次实名认证审核时间',
updateRealState bigint comment '实名变更认证状态',
submitRealTimes bigint comment '提交实名认证次数',
submitRealTime string comment '最新提交实名信息的时间',
verifyRealUserID string comment '最新实名认证审批管理员用户ID（管理员才允许审批）',
verifyRealTime string comment '最新审核实名信息的时间',
applyVIP bigint comment '申请VIP套餐',
applyVIPTime string comment 'VIP套餐申请时间',
vipApproveState bigint comment 'VIP审批状态',
vipApproveUserID string comment 'VIP审批管理员用户ID（管理员才允许审批）',
vipApproveTime string comment 'VIP审批时间',
feeVIP bigint comment 'VIP套餐',
feeVIPStartTime string comment 'VIP套餐开始时间',
feeVIPEndTime string comment 'VIP套餐结束时间',
spID string comment 'SP帐号ID(AES128加密)',
whereFrom string comment '用户来源',
cpAlias string comment '专区账号名称，CP账号的名称',
lastUpdateTime string comment '开发者信息更新时间',
contactName string comment '联系人姓名(AES128加密)',
contactEMail string comment '联系人邮箱（AES128加密存储）',
contactPhone string comment '联系人手机（AES128加密存储）',
province string comment '开发者所在省份',
city string comment '开发者在城市',
country string comment '开发者注册国家码')
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_BASIC_INFORMATION_DM' 

数据文件推送路径：/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_BASIC_INFORMATION_DM 

XML配置文件：

           <FileToHDFS action="ODS_DEV_BASIC_INFORMATION_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_BASIC_INFORMATION_DM/*ODS_DEV_BASIC_INFORMATION_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_DEV_BASIC_INFORMATION_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>DEV/ODS_DEV_BASIC_INFORMATION_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
