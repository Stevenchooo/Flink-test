CREATE EXTERNAL TABLE ODS_VMALL2_TBL_SYS_REGION_DM(
id STRING comment '����ID',
parent_id STRING comment '�õ�ַ�ĸ���ַID',
code STRING comment '����',
name STRING comment '��ַ����',
status STRING comment '״̬',
parent_number_code STRING comment '�õ�ַ�ĸ���ַ��������ַ���Ӵ�',
create_date STRING comment '��������',
post_code STRING comment '��������')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL2_TBL_SYS_REGION_DM' 
   
�����ļ�����·����   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL2_TBL_SYS_REGION_DM

XML�����ļ���
           <FileToHDFS action="ODS_VMALL2_TBL_SYS_REGION_DM">
            <!-- �ļ�������Ϣ -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- �����ļ�����-->
            <MultiInputFileConf>
                <!-- �����ļ��б�-->
                <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL2_TBL_SYS_REGION_DM/*ODS_VMALL2_TBL_SYS_REGION_DM_*.txt.lzo</InputFileList>
                <!-- �����ļ�����  ������ó�-1���ò�����Ч��ֻ���ǵȴ�ʱ�� -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- �ȴ�����ʱ�䣨���ӣ� -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- �ļ��ֶεķָ����Ĭ��ʹ��\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- ���� -->
            <Tablename>ODS_VMALL2_TBL_SYS_REGION_DM</Tablename>
            <!-- ��洢λ�� -->
            <Tablelocation>VMALL/ODS_VMALL2_TBL_SYS_REGION_DM</Tablelocation>
            <!-- ���� none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>







