CREATE EXTERNAL TABLE ODS_HWREAD_FACTORY_VIP_DM (
usr string comment '用户I号',
scheme string comment '方案id',
channel string comment '渠道id',
productid string comment '产品id',
product_type string comment '产品类型',
action_type string comment '操作类型',
time string comment '日志时间',
subid string comment '子产品id',
expire_time string comment '过期时间',
days string comment '购买时长(天)',
amount string comment '购买金额',
discount_rate string comment '折扣',
buy_type string comment '购买类型',
consumption string comment '扣费方式',
os_platform string comment '操作平台',
phone_model string comment '机型',
client_version string comment '版本')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWREAD/ODS_HWREAD_FACTORY_VIP_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_FACTORY_VIP_DM

XML配置文件：

           <FileToHDFS action="ODS_HWREAD_FACTORY_VIP_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_FACTORY_VIP_DM/*ODS_HWREAD_FACTORY_VIP_DM*.log</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\t</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWREAD_FACTORY_VIP_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWREAD/ODS_HWREAD_FACTORY_VIP_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
