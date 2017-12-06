#----------------------------------------------------------------------------
#  file Name: v0.3 dw_app_defined_label_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2013.All rights reserved.
#  Purpose:  根据用户安装应用自定义标签活动标签信息
#  Describe: 
#  Input:  
#  Output: dw_app_defined_label_ds
#  Author:  wangweiguo 00190105
#  Creation Date  2013-08-29
#  Last Modified:  2013-08-29
#----------------------------------------------------------------------------
hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_app_defined_label_ds
(
    device_id        STRING,
    labels           STRING,
    service_id       STRING
)
PARTITIONED BY (apppackagename STRING)
ROW FORMAT DELIMITED FIELDS 
TERMINATED BY '\001' LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/label/dw_app_defined_label_ds';
"