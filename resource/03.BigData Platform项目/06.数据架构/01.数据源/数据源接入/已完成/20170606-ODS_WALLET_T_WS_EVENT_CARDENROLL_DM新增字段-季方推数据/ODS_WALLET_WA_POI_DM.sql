CREATE EXTERNAL TABLE ODS_WALLET_WA_POI_DM (
id bigint,
name string,
description string,
longitude string,
latitude string,
createTime string,
updateTime string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/WALLET/ODS_WALLET_WA_POI_DM' 

�����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_WA_POI_DM   

XML�����ļ���

           <FileToHDFS action="ODS_WALLET_WA_POI_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_WALLET_WA_POI_DM/*_ODS_WALLET_WA_POI_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_WALLET_WA_POI_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>WALLET/ODS_WALLET_WA_POI_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
		  
	
	
ODS_WALLET_T_WS_EVENT_CARDENROLL_DM
�����ֶΣ�
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (channel string comment '��������');  
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (appPackageName string comment 'Ӧ�ð���');
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (imei string comment '���ܺ��imei�ţ�����ܺ�ʹ��');
alter table ODS_WALLET_T_WS_EVENT_CARDENROLL_DM add columns (clientVersion string comment '�ͻ��˰汾��');


ODS_WALLET_EVENT_CARDSWING_DM
�����ֶΣ�
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (transTerminal string comment '�ն˻����');  
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (transType string comment '��������');
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (poiId string comment '���ܺ����Ȥ��id����Ҫ����ʹ��');
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (imei string comment '���ܺ��imei�ţ���Ҫ����ʹ��');
alter table ODS_WALLET_EVENT_CARDSWING_DM add columns (clientVersion string comment '�ͻ��˰汾��');

