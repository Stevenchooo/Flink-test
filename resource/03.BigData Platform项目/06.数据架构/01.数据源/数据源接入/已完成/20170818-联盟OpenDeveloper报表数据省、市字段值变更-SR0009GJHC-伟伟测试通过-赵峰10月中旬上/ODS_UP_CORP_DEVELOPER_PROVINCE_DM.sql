CREATE EXTERNAL TABLE ODS_UP_CORP_DEVELOPER_PROVINCE_DM(
countryCode string comment '所属国家',
chineseName string comment '中文名称',
englishName string comment '英文名称',
shortNameCn string comment '中文简称',
alpha2Code string comment 'ISO省份编码',
status string comment '有效标识Y有效N无效',
updateTime string comment 'iData的更新时间',
name string comment '省份编码',
countryName string comment '所属国家的行政区划实体对应的固定标识')
PARTITIONED BY (                                                       
   pt_d string )                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/UP/ODS_UP_CORP_DEVELOPER_PROVINCE_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_CORP_DEVELOPER_PROVINCE_DM 

XML配置文件：

           <FileToHDFS action="ODS_UP_CORP_DEVELOPER_PROVINCE_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_UP_CORP_DEVELOPER_PROVINCE_DM/*ODS_UP_CORP_DEVELOPER_PROVINCE_DM_*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_UP_CORP_DEVELOPER_PROVINCE_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>UP/ODS_UP_CORP_DEVELOPER_PROVINCE_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>