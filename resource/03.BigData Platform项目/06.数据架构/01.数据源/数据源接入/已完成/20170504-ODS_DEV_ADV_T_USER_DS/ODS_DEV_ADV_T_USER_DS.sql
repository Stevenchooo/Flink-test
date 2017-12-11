CREATE EXTERNAL TABLE IF NOT EXISTS ODS_DEV_ADV_T_USER_DS
(
    user_id            STRING          COMMENT   '广告系统中的userID',
    UpID               STRING          COMMENT   'UP中的userID，来自t_user表的login_name'
)
COMMENT '开放平台广告任务话单'
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '|'
LINES TERMINATED BY '\n'
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '/AppData/OpenAllianceProd/OpenAlliance/data/ODS/DEV/ODS_DEV_ADV_T_USER_DS';