CREATE EXTERNAL TABLE ODS_BISDK_WEB_DM (
typeId string comment '����ID',
link string comment '���(������)������',
revenue string comment '����(δʹ��)',
idsite string comment 'ID',
hour string comment '�ͻ���ʱ��(Сʱ)',
min string comment '�ͻ���ʱ��(����)',
sec string comment '�ͻ���ʱ��(��)',
url string comment '������ҳ��url',
urlref string comment '��һ��url(δʹ��)',
refpath string comment '(δʹ��)',
id string comment 'Cookie(Ψһ��16λ����16�����ַ���)',
idts string comment 'Cookie����ʱ��',
idvc string comment '���ʴ���',
idn string comment '�·�����',
rcn string comment 'δʹ��',
rck string comment 'δʹ��',
refts string comment '������Դʱ��',
viewts string comment '���һ�η���ʱ��',
scd string comment '��Ļ��ɫ���',
vpr string comment '���ڷֱ���',
ects string comment '�ϴζ���ʱ��(δʹ��)',
ref string comment '������Դurl(δʹ��)',
cvar_pg string comment '�û��Զ���page����',
java string comment 'java���֧��',
pdf string comment 'pdf���֧��',
qt string comment 'quicktime���֧��',
realp string comment 'realaudio���֧��',
wma string comment 'wma���֧��',
dir string comment 'director���֧��',
fla string comment 'flash���֧��',
gears string comment 'gears���֧��',
ag string comment 'silverlight���֧��',
res string comment '�ֱ���',
cookie string comment '�Ƿ�֧��cookie',
data string comment '�û��Զ�������',
cvar_vi string comment '�û��Զ���visit����(δʹ��)',
serverts string comment 'serverts',
ip string comment 'ip',
os string comment 'os',
browser string comment '�����',
version string comment '�汾',
language string comment '����',
sid string comment 'sid',
ua string comment 'ua')
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_WEB_DM' 

   �����ļ�����·����/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_WEB_DM   

XML�����ļ���

           <FileToHDFS action="ODS_BISDK_WEB_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_BISDK_WEB_DM/*ODS_BISDK_WEB_DM*.txt</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_BISDK_WEB_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>BISDK/ODS_BISDK_WEB_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
