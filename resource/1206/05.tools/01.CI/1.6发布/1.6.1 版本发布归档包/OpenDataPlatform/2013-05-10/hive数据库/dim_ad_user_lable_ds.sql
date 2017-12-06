# ----------------------------------------------------------------------------
#  File Name: dim_ad_user_label_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2011.All rights reserved.
#  Purpose: 获取push业务中机型与rom版本统计信息
#  Describe:push业务中机型与rom版本统计信息
#  Input:  DIM_USER_LABEL_DS  (push宽表统计维表),
#  Output: dim_ad_user_label_ds 
#  Author: wangweiguo/w00190105
#  Creation Date: 2013-04-30
#  Last Modified: 2013-04-30
# ----------------------------------------------------------------------------
hive -e "
CREATE EXTERNAL TABLE IF NOT EXISTS dim_ad_user_label_ds
(  
    device_id                                  STRING,
    birthday                                   STRING,
    gender                                     STRING,
    register_area_id                           BIGINT,
    register_parent_area_id                    BIGINT,
    register_time                              STRING,
    account_type                               INT,
    account_state                              INT,
    terminal_type                              STRING,
    terminal_os_version                        STRING,
    screen_resolution                          STRING,
    mobile_oper                                STRING,
    service_client_version                     STRING,
    push_flag                                  TINYINT,
    user_ad_flag                               TINYINT,
    push_version                               STRING,
    ad_type_flag                               TINYINT,
    dormant_flag                               INT,
    longitudinal_category                      STRING,
    latitudinal_category                       STRING,
    rom_version                                STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '|'
LINES TERMINATED BY '\n'
STORED AS TEXTFile
LOCATION '$hadoopuser/data/iMarketing/dim_ad_user_label_ds';


INSERT OVERWRITE TABLE dim_ad_user_label_ds
SELECT 
    device_id                   as          device_id,
    birthday                    as          birthday,
    gender                      as          gender,
    register_area_id            as          register_area_id,
    register_parent_area_id     as          register_parent_area_id,
    register_time               as          register_time,
    account_type                as          account_type,
    account_state               as          account_state,
    terminal_type               as          terminal_type,
    terminal_os_version         as          terminal_os_version,
    screen_resolution           as          screen_resolution,
    mobile_oper                 as          mobile_oper,
    service_client_version      as          service_client_version,
    push_flag                   as          push_flag,
    user_ad_flag                as          user_ad_flag,
    push_version                as          push_version,
    ad_type_flag                as          ad_type_flag,
    dormant_flag                as          dormant_flag,
    longitudinal_category       as          longitudinal_category,
    latitudinal_category        as          latitudinal_category,
    rom_version                 as          rom_version
FROM
    DIM_USER_LABEL_DS
WHERE
    ad_type_flag = 1;
"