CREATE EXTERNAL TABLE ODS_HWREAD_DOWNLOAD_PAY_DM(
usr string comment '用户I号',                   
book_id string comment '购买书籍iD',                          
price string comment '总付费金额',              
count string comment '购买章节数',              
channel string comment '渠道ID',                                             
phone_model string comment '手机型号',         
book_type string comment '书籍计费类型',           
down_type string comment '下载类型',           
chapter_id string comment '下载起始章节Id',         
fee_type string comment '账户类型',           
main_account_pay string comment '主账户扣费金额',     
seecondary_account_pay string comment '副账户扣费金额',  
time string comment '购买时间',                             
order_id string comment '订单ID',           
client_version string comment '版本号',                            
os_platform string comment '平台',          
sms_account_pay string comment '短信账户扣费金额',            
pay_type string comment '仅连载书按章自动购买时有值',          
balance string comment '用户账户余额')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWREAD/ODS_HWREAD_DOWNLOAD_PAY_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_DOWNLOAD_PAY_DM

XML配置文件：

           <FileToHDFS action="ODS_HWREAD_DOWNLOAD_PAY_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_DOWNLOAD_PAY_DM/*ODS_HWREAD_DOWNLOAD_PAY_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWREAD_DOWNLOAD_PAY_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWREAD/ODS_HWREAD_DOWNLOAD_PAY_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>