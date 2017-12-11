CREATE EXTERNAL TABLE ODS_HWREAD_FACTORY_CHARGE_DM (
id string comment '流水ID',
order_id string comment '订单号',
usr string comment '用户I号',
scheme string comment '方案Id',
phone_model string comment '手机型号',
channel string comment '渠道ID',
amount string comment '充值金额',
gift_amount string comment '代金卷金额',
seq_type string comment '流水类型',
recharging_type string comment '充值类型',
phone string comment '电话',
card_num string comment '银行卡卡号',
status string comment '订单状态',
status_code string comment '第三方系统响应的状态码',
status_desc string comment '第三方系统响应的状态描述',
src_data string comment '业务参数',
before_amount string comment '充值前用户账户总金额',
after_amount string comment '充值后用户账户总金额',
create_time string comment '充值订单创建时间',
reserve1 string comment '交易码',
reserve3 string comment 'IMEI',
reserve6 string comment '充值订单提交时间',
reserve7 string comment '在订单处理完成的流水中是充值订单收到第三方网关通知时间')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='\t',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='\t')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/HWREAD/ODS_HWREAD_FACTORY_CHARGE_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_FACTORY_CHARGE_DM

XML配置文件：

           <FileToHDFS action="ODS_HWREAD_FACTORY_CHARGE_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_FACTORY_CHARGE_DM/*ODS_HWREAD_FACTORY_CHARGE_DM*.log</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\t</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWREAD_FACTORY_CHARGE_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWREAD/ODS_HWREAD_FACTORY_CHARGE_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>