# ----------------------------------------------------------------------------
#  File Name: dim_terminal_romversion_info_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2011.All rights reserved.
#  Purpose: 获取push业务中机型与rom版本统计信息
#  Describe:push业务中机型与rom版本统计信息
#  Input:  DIM_USER_LABEL_DS  (push宽表统计维表),
#  Output: dim_terminal_romversion_info_ds 
#  Author: wangweiguo/w00190105
#  Creation Date: 2013-04-30
#  Last Modified: 2013-04-30
# ----------------------------------------------------------------------------
hive -e "
CREATE EXTERNAL TABLE IF NOT EXISTS dim_terminal_romversion_info_ds
(  
    terminal_type        STRING,
    rom_version          STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/iMarketing/dim_terminal_romversion_info_ds';


add jar $HIBI_PATH/udflib/huawei_udf.jar;
create temporary function RomVersionCheckUDF as 'com.huawei.udf.RomVersionCheckUDF';


INSERT OVERWRITE TABLE dim_terminal_romversion_info_ds
SELECT
        t.terminal_type   as    terminal_type,
        t.rom_version     as    rom_version
FROM
(
    SELECT
        terminal_type   as    terminal_type,
        rom_version     as    rom_version,
        count(*)        as    cnt
    FROM
        DIM_USER_LABEL_DS
    WHERE
        RomVersionCheckUDF(rom_version)
    AND terminal_type is not null
    GROUP BY terminal_type,rom_version
)t
WHERE
    t.cnt > 100;
 "