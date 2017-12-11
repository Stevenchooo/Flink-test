CREATE EXTERNAL TABLE ODS_HOTA_UPDATE_DEVICEINFO_HM (
IMEI string,
PLMN string,
ClientIP string,
createTime string,
CheckTime string,
Result string,
DeviceName string,
FirmwareVersion string,
HardwareVersion string,
DashbordVersion string,
DashboardFlashVersion string,
AppName string,
ClientOS string,
OSLanguage string,
ClientLanguage string,
Cversion string,
Network string,
WebUIVersion string,
WebUICVer string,
Firmware1CVer string,
D_version string,
ResultMsg string,
SaleInfo string,
ControlFlag string,
PackageType string,
extra_info string,
RouterFeature string,
SiteCode string)
PARTITIONED BY (                                                       
   pt_d string,pt_h string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'='`',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'='`')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/UP/ODS_HOTA_UPDATE_DEVICEINFO_HM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HOTA_UPDATE_DEVICEINFO_HM

XML配置文件：

           <FileToHDFS action="ODS_HOTA_UPDATE_DEVICEINFO_HM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HOTA_UPDATE_DEVICEINFO_HM/*ODS_HOTA_UPDATE_DEVICEINFO_HM*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>`</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HOTA_UPDATE_DEVICEINFO_HM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>UP/ODS_HOTA_UPDATE_DEVICEINFO_HM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d,pt_h</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>