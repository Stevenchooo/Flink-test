CREATE EXTERNAL TABLE ODS_HNREAD_USER_ACTION_DM (
statis_day comment '日期',
userid string comment '设备id',
channel string comment '渠道号',
version string comment '版本号',
account string comment 'QQ',
openid string comment 'Openid',
action_type string comment '行为类型',
bookid string comment '书籍id',
bookname string comment '书籍名称',
sexattr string comment '品类',
two_level string comment '二级分类',
words string comment '字数',
createtime string comment '上架时间',
free string comment '是否免费',
unitprice string comment '千字单价',
author string comment '作者',
pay_type string comment '购买方式',
consume_price string comment '消耗书币_book',
quan_cnt string comment '消耗书券_book',
is_vip string comment '是否vip',
vip_cost string comment '消耗书币_vip',
endtime_vip string comment 'vip到期时间',
charge_coin string comment '充值金额',
charge_pv string comment '充值次数',
account_coin string comment '账户余额_coin',
account_quan string comment '账户余额_quan')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HNREAD/ODS_HNREAD_USER_ACTION_DM' 

数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HNREAD_USER_ACTION_DM

XML配置文件：

           <FileToHDFS action="ODS_HNREAD_USER_ACTION_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HNREAD_USER_ACTION_DM/*ODS_HNREAD_HWRY_ACTION_DM*.txt</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\|</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_HNREAD_USER_ACTION_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>HNREAD/ODS_HNREAD_USER_ACTION_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
