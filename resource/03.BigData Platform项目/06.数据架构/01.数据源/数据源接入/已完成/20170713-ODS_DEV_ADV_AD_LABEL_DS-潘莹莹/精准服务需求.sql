1,ODS_DEV_ADV_T_USER_DS
新增字段
alter table ODS_DEV_ADV_T_USER_DS add columns (user_type string comment '用户类型'); 

2,ODS_DEV_ADV_T_TASK_DS
新增字段
alter table ODS_DEV_ADV_T_TASK_DS add columns (delete_flag string comment '任务删除标记'); 
alter table ODS_DEV_ADV_T_TASK_DS add columns (budget string comment '广告任务预算'); 

4.ODS_DEV_ADV_ACCESS_HM
新增字段
alter table ODS_DEV_ADV_ACCESS_HM add columns (labelId string comment '广告标签ID'); 

5.ODS_DEV_ADV_CLICK_HM
新增字段
alter table ODS_DEV_ADV_CLICK_HM add columns (labelId string comment '广告标签ID'); 

6.ODS_DEV_ADV_OTHER_HM
新增字段
alter table ODS_DEV_ADV_OTHER_HM add columns (labelId string comment '广告标签ID'); 

7.ODS_DEV_ADV_REQUEST_HM
新增字段
alter table ODS_DEV_ADV_REQUEST_HM add columns (labelId string comment '广告标签ID'); 

8,ODS_DEV_ADV_AD_LABEL_DS
新增表
CREATE EXTERNAL TABLE ODS_DEV_ADV_AD_LABEL_DS(
label_id string comment '标签ID',
label_name string comment '标签名称',
label_level string comment '标签等级',
parent_id string comment '父节点ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_ADV_AD_LABEL_DS' 
   
数据文件推送路径：   /MFS/DataIn/OpenAliance/odsdata/ODS_DEV_ADV_AD_LABEL_DS/

XML配置文件：

           <FileToHDFS action="ODS_DEV_ADV_AD_LABEL_DS">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/OpenAliance/odsdata/ODS_DEV_ADV_AD_LABEL_DS/*ODS_DEV_ADV_AD_LABEL_DS_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_DEV_ADV_AD_LABEL_DS</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>DEV/ODS_DEV_ADV_AD_LABEL_DS</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>