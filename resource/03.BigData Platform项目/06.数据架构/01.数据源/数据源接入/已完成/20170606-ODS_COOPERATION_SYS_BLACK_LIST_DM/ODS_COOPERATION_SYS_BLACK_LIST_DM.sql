CREATE EXTERNAL TABLE ODS_COOPERATION_SYS_BLACK_LIST_DM (
uid string,
mpAccount string,
createTime string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/COOPERATION/ODS_COOPERATION_SYS_BLACK_LIST_DM'    
   
数据文件推送路径：   /MFS/DataIn/phoneservice/odsdata/ODS_COOPERATION_SYS_BLACK_LIST_DM/


XML配置文件：
           <FileToHDFS action="ODS_COOPERATION_SYS_BLACK_LIST_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/phoneservice/odsdata/ODS_COOPERATION_SYS_BLACK_LIST_DM/*_ODS_COOPERATION_SYS_BLACK_LIST_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_COOPERATION_SYS_BLACK_LIST_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>COOPERATION/ODS_COOPERATION_SYS_BLACK_LIST_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
