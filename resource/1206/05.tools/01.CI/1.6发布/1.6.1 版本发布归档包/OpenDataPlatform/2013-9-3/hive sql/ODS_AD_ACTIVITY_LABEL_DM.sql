#----------------------------------------------------------------------------
#  file Name: v0.3 ODS_AD_ACTIVITY_LABEL_DM.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2013.All rights reserved.
#  Purpose:  广告自定义标签
#  Describe: 
#  Input:  
#  Output: ODS_AD_ACTIVITY_LABEL_DM
#  Author:  wangweiguo 00190105
#  Creation Date  2013-08-29
#  Last Modified:  2013-08-29
#----------------------------------------------------------------------------
hive -e "
CREATE EXTERNAL TABLE IF NOT EXISTS ODS_AD_ACTIVITY_LABEL_DM
(  
    activity_id            string,
    event                  string,
    labels                 string
)
PARTITIONED BY (pt_d STRING)
ROW format delimited
FIELDS terminated by '\001'
LINES terminated by '\n'
STORED AS TEXTFILE
LOCATION '$hadoopuser/data/ODS/AD/ODS_AD_ACTIVITY_LABEL_DM'
"