CREATE EXTERNAL TABLE ODS_VMALL_PMS_SBOM_DM (
id bigint comment 'sbom标识',
name String comment 'SBOM名字(展示名称)',
code String comment 'SBOM编码',
gbom_code String comment 'GBOM编码',
publish_id bigint comment '发布ID',
product_id bigint comment '商品ID',
carrier_id bigint comment '运营商ID',
channel_id bigint comment '渠道ID',
sec_channel_id bigint comment '二级渠道ID',
micro_prom_word String comment '微型促销语',
sale_type bigint comment 'SKU销售类型',
price_mode bigint comment '价格显示方式',
button_mode bigint comment '购买类型',
tips_content String comment '详情页提示显示内容',
limited_quantity bigint comment '限购数量',
status bigint comment '状态',
SEOTitle String comment 'SEO标题',
SEOKeyword String comment 'SEO关键字',
create_by String comment '创建人',
create_time String comment '创建时间',
update_by String comment '修改人',
update_time String comment '修改时间',
sync_status bigint comment '同步状态',
default_sbom bigint comment '默认SBOM',
sbom_abbr String comment 'sbom简称',
is_search bigint comment '是否被检索',
prd_display_id bigint comment '展示商品ID',
sbom_prom_word String comment 'SBOM（长）促销语',
sbom_prom_word_link String comment '促销语的链接地址',
shop_prd_code String comment '商铺的商品（服务）编码',
order_by_column1 bigint comment '排序1，国内存放评论',
order_by_column2 bigint comment '排序2，国内存放销量',
order_by_column3 bigint comment '排序3，预留',
is_identity_img bigint comment '是否含身份证图',
sms_task_code String comment '开售提醒编号',
timer_prom_word String comment '定时促销语',
timer_prom_link4PC String comment '定时促销语PC链接地址',
timer_prom_link4WAP String comment '定时促销语WAP链接地址',
timer_prom_link4APP String comment '定时促销语APP链接地址',
timer_prom_starttime String comment '定时促销语展示开始时间',
timer_prom_endtime String comment '定时促销语展示结束时间',
use_sms_task_code bigint comment '是否启用开售提醒',
product_note String comment '商品备注',
privi_code String comment '权益编码（保险分类）',
warranty_time bigint comment '服务月份')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_SBOM_DM' 
   
数据文件推送路径：   /MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_SBOM_DM

XML配置文件：
           <FileToHDFS action="ODS_VMALL_PMS_SBOM_DM">
            <!-- 文件配置信息 -->
            <Fileconf>ODS_Hota</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_PMS_SBOM_DM/*ODS_VMALL_PMS_SBOM_DM_*.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VMALL_PMS_SBOM_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VMALL/ODS_VMALL_PMS_SBOM_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
            <CompressType>orc_zlib</CompressType>
          </FileToHDFS>
