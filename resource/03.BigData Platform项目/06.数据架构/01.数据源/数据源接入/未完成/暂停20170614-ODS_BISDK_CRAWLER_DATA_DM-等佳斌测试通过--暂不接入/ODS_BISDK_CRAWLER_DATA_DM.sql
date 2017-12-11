CREATE EXTERNAL TABLE ODS_BISDK_CRAWLER_DATA_DM (
data string,
task_id string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_CRAWLER_DATA_DM'   
   
   
   �����ļ�����·����   /MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_CRAWLER_DATA_DM/
   
   
   XML�����ļ���
           <FileToHDFS action="ODS_BISDK_CRAWLER_DATA_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_CRAWLER_DATA_DM/*_CRAWLER_DATA_DM_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_BISDK_CRAWLER_DATA_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>BISDK/ODS_BISDK_CRAWLER_DATA_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>