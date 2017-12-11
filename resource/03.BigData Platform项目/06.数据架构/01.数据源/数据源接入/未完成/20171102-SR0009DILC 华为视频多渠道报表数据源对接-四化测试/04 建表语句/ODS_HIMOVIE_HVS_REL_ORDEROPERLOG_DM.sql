CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM(
subscriberid string comment '订户编号',
pid string comment '产品编号',
serviceid string comment 'PackageID',
contentid string comment '按次订购的内部编号',
contenttype string comment '按次订购的内部类型',
businessid string comment '固定值0',
starttime string comment '有效开始时间',
endtime string comment '有效结束时间',
priceobjecttype string comment '定价对象的类型',
virturedeviceid string comment '产品绑定的逻辑设备ID',
identid string comment 'Profile编号',
ordertype string comment '订购类型',
chargemode string comment '订购方式',
promocode string comment '促销码',
servicepaytype string comment '支付方式',
fee string comment '费用',
prediscountfee string comment '折扣前费用',
residualchoosenum string comment '自选套餐剩余可选数量',
isalacarteid string comment '是否是自选套餐',
subscribetype string comment '产品类型',
productorderkey string comment '订购关系主键',
channel string comment '后台订购默认值',
customfields string comment '扩展字段',
originaldevicemodel string comment '发起订购、退订的设备类型',
physicaldeviceid string comment '发起订购、退订的物理设备ID',
subpaymenttype string comment '子支付方式',
iscontinue string comment '是否续订',
actiontype string comment '操作类型',
actiontime string comment '操作时间',
starttimeutc string comment '产品生效时间(UTC)',
endtimeutc string comment '产品失效时间(UTC)',
actiontimeutc string comment '订购时间(UTC)',
subscribersn string comment '订户内部编号',
profilesn string comment 'Profile内部编号',
deviceoriginal string comment 'VSP原始物理设备ID',
subscriptionresult string comment '订购结果码',
actionresult string comment '操作是否成功',
subscriptionstate string comment '状态',
prod_version string comment '产品version',
cycleendtime string comment '本周期结束时间',
cancelordertime string comment '取消续订时间',
cycleendtimeutc string comment '本周期结束时间UTC',
cancelordertimeutc string comment '取消续订时间UTC',
orderno string comment '外部订单号')
PARTITIONED BY ( pt_d string )
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM/*_ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_ORDEROPERLOG_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>