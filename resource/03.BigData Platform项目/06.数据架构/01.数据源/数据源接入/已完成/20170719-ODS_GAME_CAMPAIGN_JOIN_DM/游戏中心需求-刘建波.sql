1,��Ϸȯ������ϸ����Դ:
CREATE EXTERNAL TABLE ODS_GAME_COUPON_DELIVER_DETAILS_DM (
campaignId bigint comment '�ID',
userId string comment '��Ϊ�˺�ID',
appId string comment '��ϷID���û���ֵ����Ϸ',
spend string comment '�ֽ����ѽ��',
amount string comment '������Ϸȯ���',
deliverTime string comment '��Ϸȯ����ʱ��',
useAppId string comment '����ȯ��Ӧ����ϷID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_COUPON_DELIVER_DETAILS_DM' 
   
�����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_COUPON_DELIVER_DETAILS_DM   

XML�����ļ���

           <FileToHDFS action="ODS_GAME_COUPON_DELIVER_DETAILS_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_COUPON_DELIVER_DETAILS_DM/*ODS_GAME_COUPON_DELIVER_DETAILS_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_GAME_COUPON_DELIVER_DETAILS_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>GAME/ODS_GAME_COUPON_DELIVER_DETAILS_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
2,�����ҳ����Դ:
CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM (	  
campaignId bigint comment '�ID',
userId string comment '��Ϊ�˺�ID',
imei string comment '�ֻ�IMEI',
time string comment '���ʻҳ��ʱ��')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM' 
   
�����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM   

XML�����ļ���

           <FileToHDFS action="ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM/*ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_PAGE_ACCESS_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
3,��μ�����Դ:
CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_JOIN_DM (
campaignId bigint comment '�ID',
userId string comment '��Ϊ�˺�ID',
imei string comment '�ֻ�IMEI',
time string comment '�μӻʱ��',
awardIds string comment '��ƷID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_JOIN_DM' 
   
�����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_JOIN_DM   

XML�����ļ���

           <FileToHDFS action="ODS_GAME_CAMPAIGN_JOIN_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_JOIN_DM/*ODS_GAME_CAMPAIGN_JOIN_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_GAME_CAMPAIGN_JOIN_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_JOIN_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  
4,�Ӧ����������Դ:	
CREATE EXTERNAL TABLE ODS_GAME_CAMPAIGN_APP_DM (  
campaignId bigint comment '�ID',
type int comment 'Ӧ����������',
appId string comment 'Ӧ��ID',
appListId int comment '��̬Ӧ������ID')
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
   'hdfs://hacluster/AppData/BIProd/ODS/GAME/ODS_GAME_CAMPAIGN_APP_DM' 
   
�����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_APP_DM   

XML�����ļ���

           <FileToHDFS action="ODS_GAME_CAMPAIGN_APP_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_GAME_CAMPAIGN_APP_DM/*ODS_GAME_CAMPAIGN_JOIN_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_GAME_CAMPAIGN_APP_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>GAME/ODS_GAME_CAMPAIGN_APP_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
