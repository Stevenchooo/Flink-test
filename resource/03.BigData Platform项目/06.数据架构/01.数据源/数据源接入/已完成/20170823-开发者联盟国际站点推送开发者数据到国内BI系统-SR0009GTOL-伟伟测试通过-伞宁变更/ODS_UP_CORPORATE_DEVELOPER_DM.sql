CREATE EXTERNAL TABLE ODS_UP_CORPORATE_DEVELOPER_DM(
userID string comment '用户ID(内部)',
corpName string comment '企业名称(实名认证项)(AES128加密)',
CorpBsLicID string comment '营业执照编号(加密存储)(实名认证项)',
corpProvince string comment '企业所在省份(实名认证项)',
corpCity string comment '企业所在城市(实名认证项)',
corpPostCode string comment '企业邮编(AES128加密)',
corpAddress string comment '企业地址(加密存储)',
LegalMan string comment '企业法人(加密存储)(实名认证项)',
LegalManCtfType bigint comment '企业法人证件类型(实名认证项)',
LegalManCtfCode string comment '企业法人证件号码(加密存储)(实名认证项)',
corpNature bigint comment '企业性质',
corpSize bigint comment '企业规模(人数)',
attachment1 string comment '附件1(企业营业执照照片) (文件加密存储)(实名认证项)',
attachment2 string comment '附件2(税务登记照片)(文件加密存储)(实名认证项)',
attachment3 string comment '附件3(组织机构代码照片)(文件加密存储)(实名认证项)',
lastUpdateTime string comment '开发者信息更新时间')
PARTITIONED BY (                                                       
   pt_d string)                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_UP_CORPORATE_DEVELOPER_DM' 

数据文件推送路径：/MFS/DataIn/OpenAlliance/odsdata/ODS_UP_CORPORATE_DEVELOPER_DM 

XML配置文件：

           <FileToHDFS action="ODS_UP_CORPORATE_DEVELOPER_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_UP_CORPORATE_DEVELOPER_DM/*ODS_UP_CORPORATE_DEVELOPER_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_UP_CORPORATE_DEVELOPER_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>DEV/ODS_UP_CORPORATE_DEVELOPER_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>