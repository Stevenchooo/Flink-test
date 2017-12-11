ODS_VIDEO_HIMOVIE_CP_BASE_INFO_DM  
�����ֶΣ�
alter table ODS_VIDEO_HIMOVIE_CP_BASE_INFO_DM  add columns (hvsSubNetId string comment 'HVS������ID');  

ODS_HWMOVIE_PROMOTION_CDR_DM
�����ֶΣ�
alter table ODS_HWMOVIE_PROMOTION_CDR_DM  add columns (beId string comment '��Ӫ����ID');  
alter table ODS_HWMOVIE_PROMOTION_CDR_DM  add columns (timeZone string comment 'ʱ����Ϣ');  

ODS_HWMOVIE_EPG_ACCESS_STAT_DM
�����ֶΣ�
alter table ODS_HWMOVIE_EPG_ACCESS_STAT_DM  add columns (timeZone  string comment 'ʱ����Ϣ');  

ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM
�����ֶΣ�
alter table ODS_HWMOVIE_EPG_SUBSCRIBER_CDR_DM  add columns (timeZone  string comment 'ʱ����Ϣ');  

ODS_HWMOVIE_REL_PLAY_RECORD_VMOS_DM
�����ֶΣ�
alter table ODS_HWMOVIE_REL_PLAY_RECORD_VMOS_DM  add columns (hvsSubNetId string comment 'HVS������ID'); 
alter table ODS_HWMOVIE_REL_PLAY_RECORD_VMOS_DM  add columns (timeZone  string comment 'ʱ����Ϣ');  

ODS_HWMOVIE_SUBSCRIBER_RENTINFO_DM
�����ֶΣ�
alter table ODS_HWMOVIE_SUBSCRIBER_RENTINFO_DM  add columns (hvsSubNetId string comment 'HVS������ID'); 
alter table ODS_HWMOVIE_SUBSCRIBER_RENTINFO_DM  add columns (timeZone  string comment 'ʱ����Ϣ');  

ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM
�����ֶΣ�
alter table ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM  add columns (beId string comment 'Ĭ��Ϊ��Ϊ��Ƶ�й�����Ӫ������ʶ'); 
alter table ODS_VIDEO_CLOUD_USER_OPERATE_LOG_DM  add columns (timeStampUTC  string comment 'ʱ��');  

������
ODS_VIDEO_CLOUD_APPID_INFO_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_APPID_INFO_DM(
appId String comment '��Ƶ��APPID',
brandId String comment 'APP������Ʒ����Ϣ',
appNameZH String comment 'APP��������',
appNameEN String comment 'APPӢ������',
appType	String comment 'APP������',
packageName String comment '����',
minVersionCode String comment '��Ͱ汾',
maxVersionCode String comment '��߰汾',
appSignType int comment 'Ӧ�ñ�ʶ')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_APPID_INFO_DM' 

�����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_INFO_DM 

XML�����ļ���

           <FileToHDFS action="ODS_VIDEO_CLOUD_APPID_INFO_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_INFO_DM/*ODS_VIDEO_CLOUD_APPID_INFO_DM_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_VIDEO_CLOUD_APPID_INFO_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_APPID_INFO_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  

������
ODS_VIDEO_CLOUD_APPID_MAP_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_APPID_MAP_DM(
appId String comment '��Ƶ�Ʒ����appid����',
upAppId String comment '�˺ŷ����appid',
payAppId String comment '֧�������appid')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_APPID_MAP_DM' 

�����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_MAP_DM 

XML�����ļ���

           <FileToHDFS action="ODS_VIDEO_CLOUD_APPID_MAP_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_APPID_MAP_DM/*ODS_VIDEO_CLOUD_APPID_MAP_DM_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_VIDEO_CLOUD_APPID_MAP_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_APPID_MAP_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

	  
������	  
ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM(
beId String comment '��Ӫ����ID',
contoryCode String comment '��λ��ĸ����',
countryCallingCode String comment '��Ͱ汾',
isDefaultCountry int comment '�Ƿ�Ĭ�Ϲ���',
countryNameZH String comment '����������',
countryNameEN String comment '����Ӣ����',
defaultLanguage String comment '���ұ�׼����',
language String comment '֧�ֵ�����',
timeZone String comment '���ұ�׼ʱ��',
currencyCode String comment '���ұ�׼����',
fractionalCurrency String comment '���ұ�׼��������')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM' 

�����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM 

XML�����ļ���

           <FileToHDFS action="ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM/*ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_BE_CONTROY_MAP_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
������	  
ODS_VIDEO_CLOUD_BE_INFO_DM
CREATE EXTERNAL TABLE ODS_VIDEO_CLOUD_BE_INFO_DM(	  
beId String comment '������ʶ',
beNameZH String comment '������������',
beNameEN String comment '����Ӣ������',
brandId String comment '����Ʒ��',
beType int comment '��������',
status int comment '״̬',
siteId String comment '����վ��ID',
defaultCountry String comment '���ұ�ʶ',
appType String comment '��Ӫ�������ն�����',
hvsSubNetId String comment '������Ӧ��HVS��������ʶ',
hvsAreaId String comment '������Ӧ��HVS�������ʶ',
hvsBossId String comment '������Ӧ��HVS��BossId')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_VIDEO_CLOUD_BE_INFO_DM' 

�����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_INFO_DM 

XML�����ļ���

           <FileToHDFS action="ODS_VIDEO_CLOUD_BE_INFO_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_BE_INFO_DM/*ODS_VIDEO_CLOUD_BE_INFO_DM_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_VIDEO_CLOUD_BE_INFO_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_BE_INFO_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
