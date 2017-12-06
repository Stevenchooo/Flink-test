#----------------------------------------------------------------------------
#  file Name: v0.3 dw_user_defined_label_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2013.All rights reserved.
#  Purpose:  广告自定义标签活动标签信息
#  Describe: 
#  Input:  dw_hiad_user_defined_label_ds
#  Output: dw_user_defined_label_ds
#  Author:  wangweiguo 00190105
#  Creation Date  2013-08-29
#  Last Modified:  2013-08-29
#----------------------------------------------------------------------------
hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_user_defined_label_ds
(
    device_id        STRING,
    labels           STRING
)
PARTITIONED BY (pt_w STRING)
ROW FORMAT DELIMITED FIELDS 
TERMINATED BY '\001' LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/label/dw_user_defined_label_ds';

add jar $HIBI_PATH/udflib/LabelSTest.jar;
create temporary function DealLabelsUDAF as 'com.huawei.udaf.DealLabelsUDAF';

INSERT OVERWRITE TABLE dw_user_defined_label_ds
PARTITION (pt_w = '$week')
select
    device_id                   as      device_id,
    DealLabelsUDAF(labels)      as      labels
from
    dw_hiad_user_defined_label_ds
group by 
    device_id 
cluster by 
    device_id;
"