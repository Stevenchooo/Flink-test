CREATE EXTERNAL TABLE ODS_HWREAD_DOWNLOAD_ACTION_DM(
datetime string comment '用户下载时间',          
ip string comment 'ip地址',          
url string comment '下载url',          
usr string comment '用户I号',          
bookid string comment '下载书籍Id',          
orderid string comment '订单Id',          
startchapterid string comment '批量下载开始章节',          
endchapterid string comment '批量下载结束章节',          
down_type string comment '下载类型',          
fee_unit string comment '书籍计费类型',          
price string comment '书籍下载金额',          
channel string comment '渠道ID',          
innerver string comment '版本号',          
runid string comment '平台号',          
sim_num string comment 'sim卡号',          
imei string comment 'imei',          
sim_type string comment 'sim卡类型',          
phone_model string comment '手机型号',          
cid string comment '下载章节数',          
price_code string comment '计费类型',          
book_status string comment '书籍状态',          
original_cost string comment '书籍原价')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWREAD/ODS_HWREAD_DOWNLOAD_ACTION_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_DOWNLOAD_ACTION_DM

XML配置文件：

           <FileToHDFS action="ODS_HWREAD_DOWNLOAD_ACTION_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_DOWNLOAD_ACTION_DM/*ODS_HWREAD_DOWNLOAD_ACTION_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HWREAD_DOWNLOAD_ACTION_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HWREAD/ODS_HWREAD_DOWNLOAD_ACTION_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>