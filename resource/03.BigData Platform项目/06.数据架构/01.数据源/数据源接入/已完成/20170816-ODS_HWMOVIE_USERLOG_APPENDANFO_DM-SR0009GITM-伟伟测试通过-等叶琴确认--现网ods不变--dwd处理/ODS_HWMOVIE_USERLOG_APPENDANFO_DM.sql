CREATE EXTERNAL TABLE ODS_HWMOVIE_USERLOG_APPENDANFO_DM (
timeStamp string comment 'ʱ��',
userId bigint comment '�û�Id',
clientIP string comment '�ͻ���IP',
deviceType string comment '�豸����',
deviceId string comment '�豸��ʾId',
appId string comment 'Ӧ��Id',
packageName string comment 'Ӧ�ð���',
appVersion string comment 'Ӧ�ð汾��',
clientOS string comment '�ͻ��˲���ϵͳ',
action string comment '�û���Ϊ',
result string comment '��Ӧ��',
responseTime bigint comment '��Ӧ��ʱ',
appendInfo string comment '��չ�ֶ�',
beId String comment 'Ĭ��Ϊ��Ϊ��Ƶ�й�����Ӫ������ʶ',
timeStampUTC String comment 'ʱ��',
PARTITIONED BY (                                                       
   pt_d string comment '�����')                                                       
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
   'hdfs://hacluster/AppData/BIProd/ODS/HWMOVIE/ODS_HWMOVIE_USERLOG_APPENDANFO_DM' 

�����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_USERLOG_APPENDANFO_DM 

XML�����ļ���

           <FileToHDFS action="ODS_HWMOVIE_USERLOG_APPENDANFO_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_USERLOG_APPENDANFO_DM/*ODS_HWMOVIE_USERLOG_APPENDANFO_DM_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_HWMOVIE_USERLOG_APPENDANFO_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>HWMOVIE/ODS_HWMOVIE_USERLOG_APPENDANFO_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>