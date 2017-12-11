CREATE EXTERNAL TABLE ODS_MUSIC_DASHBOARD_HOSTING_DM (
date String comment '����',
projectid String  comment  '��Ŀid',         
projectname String comment '��Ŀ����',        
totalincome bigint comment  '������', 
contentincome bigint comment '��������',    
revenuepackage bigint comment  '��Ա����',
totaluser bigint comment  '�����û���',
freeuser bigint comment '��ѻ�Ա��',
unfreeuser bigint comment '�շѻ�Ա��',
graceuser bigint comment '�����ڻ�Ա��',
clubpurchasenum  bigint comment '��Ա�۷Ѵ���',
contentcount  bigint  comment '���ݹ������',
packagenewsuccess  bigint  comment '������Ա����',
packagerenewalchange bigint comment '�����Ա����',
packageoptout  bigint comment '�˶���Ա����',
userlogin  bigint comment '��¼�û���',
uniquevisit bigint comment 'UV',
monthuv bigint comment 'UV(30days)',
streamtimes  bigint comment '��������',
loginstreamtimes  bigint comment '��������(��½��)',
streamingusers  bigint comment '�����û���',
avgstreamingtimes String comment 'ƽ������ȫ����',
avgstreaminglen  String comment 'ƽ������ʱ��(min)')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'=',',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'=',')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/MUSIC/ODS_MUSIC_DASHBOARD_HOSTING_DM' 

   �����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_DASHBOARD_HOSTING_DM   

XML�����ļ���

           <FileToHDFS action="ODS_MUSIC_DASHBOARD_HOSTING_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_DASHBOARD_HOSTING_DM/*ODS_MUSIC_DASHBOARD_HOSTING_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>,</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_MUSIC_DASHBOARD_HOSTING_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>MUSIC/ODS_MUSIC_DASHBOARD_HOSTING_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
	  
	  
	  

CREATE EXTERNAL TABLE ODS_MUSIC_USERACTIVITY_HOSTING_DM (
dtime String comment 'ͳ�Ƶ�ʱ��',
projectid  String  comment  '��Ŀid',         
projectname String comment '��Ŀ����',        
equipmentnewusersapp bigint comment  'APP�����豸��', 
equipmentnewusersportal bigint comment 'Portal�����豸��',    
equipmenttotalusersapp bigint comment  'APP�ۼ��豸��',
equipmenttotalusersportal bigint comment  'Portal�ۼ��豸��',
dequipmentuvweb bigint comment '����UVAID���أ�������ΪWEB���豸��',
dequipmentuvwap bigint comment '����UVAID���أ�������ΪWAP���豸��',
dequipmentuvapp bigint comment '����UVAID���أ�������ΪAPP���豸��',
dequipmentuvussd  bigint comment '����UVAID���أ�������ΪUSSD���豸��',
mequipmentuvweb  bigint  comment '���30������ݣ�����UVAID���أ�������ΪWEB���豸��',
mequipmentuvwap  bigint  comment '���30������ݣ�����UVAID���أ�������ΪWAP���豸��',
mequipmentuvapp bigint comment '���30������ݣ�����UVAID���أ�������ΪAPP���豸��',
mequipmentuvussd  bigint comment '���30������ݣ�����UVAID���أ�������ΪUSSD���豸��',
equipmentuvpercent  String comment 'DAUռ��',
accountnewusers bigint comment '�����û���',
accounttotalusers bigint comment '�ۼ��û���',
daccountuvweb  bigint comment '����account���أ�������ΪWEB���û���',
daccountuvwap  bigint comment '����account���أ�������ΪWAP���û���',
daccountuvapp  bigint comment '����account���أ�������ΪAPP���û���',
daccountuvussd bigint comment '����account���أ�������ΪUSSD���û���',
maccountuvweb  bigint comment '���30������ݣ�����accountD���أ�������ΪWEB���û���',
maccountuvwap bigint comment  '���30������ݣ�����accountD���أ�������ΪWAP���û���', 
maccountuvapp bigint comment '���30������ݣ�����accountD���أ�������ΪAPP���û���',    
maccountuvussd bigint comment  '���30������ݣ�����accountD���أ�������ΪUSSD���û���',
accountuvpercent String comment  'DAUռ��')
PARTITIONED BY (                                                       
   pt_d string)                                                       
 ROW FORMAT SERDE                                                       
   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'                          
 WITH SERDEPROPERTIES (                                                 
   'field.delim'=',',                                                   
   'line.delim'='\n',                                                   
   'serialization.format'=',')                                          
 STORED AS INPUTFORMAT                                                  
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'                    
 OUTPUTFORMAT                                                           
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'                   
 LOCATION                                                               
   'hdfs://hacluster/AppData/BIProd/ODS/MUSIC/ODS_MUSIC_USERACTIVITY_HOSTING_DM' 

   �����ļ�����·����/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_USERACTIVITY_HOSTING_DM   

XML�����ļ���

           <FileToHDFS action="ODS_MUSIC_USERACTIVITY_HOSTING_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_MUSIC_USERACTIVITY_HOSTING_DM/*ODS_MUSIC_USERACTIVITY_HOSTING_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>,</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_MUSIC_USERACTIVITY_HOSTING_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>MUSIC/ODS_MUSIC_USERACTIVITY_HOSTING_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>