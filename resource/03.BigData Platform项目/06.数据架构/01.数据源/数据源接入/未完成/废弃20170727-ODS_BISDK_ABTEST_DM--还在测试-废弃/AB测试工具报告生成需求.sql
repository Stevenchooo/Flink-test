CREATE EXTERNAL TABLE ODS_BISDK_ABTEST_DM (
id string comment '����',
name string comment 'AB��������',
startTime string comment 'ʵ�鿪ʼʱ��',
endTime string comment 'ʵ�����ʱ��',
totalUserCount string comment '�ܲ����û���',
expectUserCount string comment 'Ԥ�ڲ�����������')
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_ABTEST_DM' 

   �����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_ABTEST_DM   

XML�����ļ���

           <FileToHDFS action="ODS_BISDK_ABTEST_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_ABTEST_DM/*ODS_BISDK_ABTEST_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_BISDK_ABTEST_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>BISDK/ODS_BISDK_ABTEST_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>

	  


CREATE EXTERNAL TABLE ODS_BISDK_STRATEGY_DM (	  
id string comment '����',
abTestId string comment 'AB����ID',
name string comment '��������',
expectUserCount string comment 'Ԥ�ڲ�����������',
realUserCount string comment 'Ԥ�ڲ�����������')
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_STRATEGY_DM' 

   �����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_STRATEGY_DM   

XML�����ļ���

           <FileToHDFS action="ODS_BISDK_STRATEGY_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_STRATEGY_DM/*ODS_BISDK_STRATEGY_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_BISDK_STRATEGY_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>BISDK/ODS_BISDK_STRATEGY_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  


	  
CREATE EXTERNAL TABLE ODS_BISDK_USER_DM (	  
stgId string comment '����ID',
imei string comment '�û�imei')
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_USER_DM' 

   �����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_USER_DM   

XML�����ļ���

           <FileToHDFS action="ODS_BISDK_USER_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_USER_DM/*ODS_BISDK_USER_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_BISDK_USER_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>BISDK/ODS_BISDK_USER_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>	  
