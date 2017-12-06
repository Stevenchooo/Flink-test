# ----------------------------------------------------------------------------
#  File Name: PUSH_VERSION_DS.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2011.All rights reserved.
#  Purpose: 获取push业务中用户的rom版本与push版本信息
#  Describe: push版本与rom版本信息表 PUSH_VERSION_DS
#  Input:  ods_push_routerecord_dm  (push用户路由表日统计表),
#  Output: PUSH_VERSION_DS 
#  Author: wangweiguo/w00190105
#  Creation Date: 2013-04-10
#  Last Modified: 2013-04-10
# ----------------------------------------------------------------------------
hive -e "
CREATE EXTERNAL TABLE IF NOT EXISTS PUSH_VERSION_DS
(
    device_id           STRING,
    rom_version         STRING,   
    push_version        STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/iMarketing/PUSH_VERSION_DS';


add jar $HIBI_PATH/udflib/huawei_udf.jar;
create temporary function IsEmptyUDF as 'com.huawei.udf.IsEmptyUDF';


INSERT OVERWRITE TABLE PUSH_VERSION_DS
SELECT 
       t1.imei                     as      device_id,
       max(t1.mode)                as      rom_version,
       max(t1.agent_version)       as      push_version
FROM   ods_push_routerecord_dm t1
       where !IsEmptyUDF(t1.imei) and !IsEmptyUDF(t1.agent_version) 
       and cast(t1.agent_version as bigint) < 9999 and t1.pt_d = '$date'
       GROUP BY t1.imei;
"