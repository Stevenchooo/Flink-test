CREATE EXTERNAL TABLE ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS (
appId String,
phone bigint,
country String,
price float)                                                     
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS' 
   
   
   文件推送路径：/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS

   
   xml配置信息：
   
          <FileToHDFS action="ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS/*_ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>DEV/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
