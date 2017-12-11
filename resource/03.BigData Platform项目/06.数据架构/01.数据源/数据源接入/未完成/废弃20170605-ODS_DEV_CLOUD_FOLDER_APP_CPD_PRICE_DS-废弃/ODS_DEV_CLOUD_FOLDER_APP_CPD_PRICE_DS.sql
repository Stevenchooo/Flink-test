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
   
   
   �ļ�����·����/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS

   
   xml������Ϣ��
   
          <FileToHDFS action="ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS/*_ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS_*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>DEV/ODS_DEV_CLOUD_FOLDER_APP_CPD_PRICE_DS</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
