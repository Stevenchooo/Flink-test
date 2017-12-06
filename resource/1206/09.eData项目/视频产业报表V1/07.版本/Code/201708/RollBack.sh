#!/bash/bin

path=/root
current_path=`pwd`


sh $path/gohive<<EOF 2>&1 |tee $current_path/Upgrade.log
use video;
drop table video.t_mid_day_newlogin_visitor;
create table video.t_mid_day_newlogin_visitor
(
projectid string,
dtime string,
userid string,
time  string,
platformid string
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day_newlogin_visitor to user mapred;

DROP TABLE T_MID_DAY_NOTCONVERT_REGISTEREDUSERS;
CREATE TABLE video.T_MID_DAY_NOTCONVERT_REGISTEREDUSERS
(
projectid string,
activitychannelid string,
activityid string,
clientid string,
dtime string,
identityid string,
time  string
)
PARTITIONED BY(
id string,
date string
)
ROW FORMAT delimited fields terminated by '\t' MAP KEYS TERMINATED BY '&' STORED AS TEXTFILE;
grant select,delete,insert on T_MID_DAY_NOTCONVERT_REGISTEREDUSERS to user mapred;

drop table video.t_mid_day01againloginregister_tmp;

drop table video.t_mid_day03againloginregister_tmp;


drop table video.t_mid_day07againloginregister_tmp;


drop table video.t_mid_day15againloginregister_tmp;


drop table video.t_mid_day30againloginregister_tmp;


drop table video.t_mid_newsubscriber_tmp;

drop table video.t_mid_video_sdk_activeusers_d;
create table video.t_mid_video_sdk_activeusers_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
activeusers  int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_activeusers_d to user mapred;

drop table video.t_mid_video_sdk_activeusers_dm;
create table video.t_mid_video_sdk_activeusers_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
activeusers  int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_activeusers_dm to user mapred;

drop table video.t_mid_video_sdk_activeusers_dw;
create table video.t_mid_video_sdk_activeusers_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
activeusers  int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_activeusers_dw to user mapred;

drop table video.t_mid_video_sdk_appdownload_d;

drop table video.t_mid_video_sdk_appdownload_dm;

drop table video.t_mid_video_sdk_appdownload_dw;

drop table video.t_mid_video_sdk_current_d;
create table video.t_mid_video_sdk_current_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
newvisitor int,
totalvisitor int,
activeusers  int,
registeruseractive int,
neworderuser int,
loginuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_current_d to user mapred;

drop table video.t_mid_video_sdk_current_dm;
create table video.t_mid_video_sdk_current_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
newvisitor int,
totalvisitor int,
activeusers  int,
registeruseractive int,
neworderuser int,
loginuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_current_dm to user mapred;

drop table video.t_mid_video_sdk_current_dw;
create table video.t_mid_video_sdk_current_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
newvisitor int,
totalvisitor int,
activeusers  int,
registeruseractive int,
neworderuser int,
loginuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_current_dw to user mapred;

drop table video.t_mid_video_sdk_loginuser_d;
create table video.t_mid_video_sdk_loginuser_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
loginuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_loginuser_d to user mapred;

drop table video.t_mid_video_sdk_loginuser_dm;
create table video.t_mid_video_sdk_loginuser_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
loginuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_loginuser_dm to user mapred;

drop table video.t_mid_video_sdk_loginuser_dw;
create table video.t_mid_video_sdk_loginuser_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
loginuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_loginuser_dw to user mapred;

drop table video.t_mid_video_sdk_neworderuser_d;
create table video.t_mid_video_sdk_neworderuser_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
neworderuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_neworderuser_d to user mapred;

drop table video.t_mid_video_sdk_neworderuser_dm;
create table video.t_mid_video_sdk_neworderuser_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
neworderuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_neworderuser_dm to user mapred;

drop table video.t_mid_video_sdk_neworderuser_dw;
create table video.t_mid_video_sdk_neworderuser_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
neworderuser int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_neworderuser_dw to user mapred;

drop table video.t_mid_video_sdk_newvisitor_d;
create table video.t_mid_video_sdk_newvisitor_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
newvisitor int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_newvisitor_d to user mapred;

drop table video.t_mid_video_sdk_newvisitor_dm;
create table video.t_mid_video_sdk_newvisitor_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
newvisitor int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_newvisitor_dm to user mapred;

drop table video.t_mid_video_sdk_newvisitor_dw;
create table video.t_mid_video_sdk_newvisitor_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
newvisitor int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_newvisitor_dw to user mapred;

drop table video.t_mid_video_sdk_reguseractive_d;
create table video.t_mid_video_sdk_reguseractive_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
registeruseractive int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_reguseractive_d to user mapred;

drop table video.t_mid_video_sdk_reguseractive_dm;
create table video.t_mid_video_sdk_reguseractive_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
registeruseractive int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_reguseractive_dm to user mapred;

drop table video.t_mid_video_sdk_reguseractive_dw;
create table video.t_mid_video_sdk_reguseractive_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
registeruseractive int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_reguseractive_dw to user mapred;

drop table video.t_mid_video_sdk_totalvisitor_d;
create table video.t_mid_video_sdk_totalvisitor_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
totalvisitor int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_totalvisitor_d to user mapred;

drop table video.t_mid_video_sdk_totalvisitor_dm;
create table video.t_mid_video_sdk_totalvisitor_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
totalvisitor int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_totalvisitor_dm to user mapred;

ddrop table video.t_mid_video_sdk_totalvisitor_dw;
create table video.t_mid_video_sdk_totalvisitor_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
totalvisitor int
)
partitioned by(
id string,
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_totalvisitor_dw to user mapred;

DROP TABLE video.t_video_2BData_data_d_hbase;

DROP TABLE video.t_video_develop_use_active_appversion_d_hbase;

DROP TABLE video.t_video_develop_user_active_d_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_user_active_d_hbase
(
key string,
projectid string,
activitychannelid string,
platformid string,
dtime string,
activeusers int,
dayactiveusers int,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
allregisteruser int,
newregisteruser int,
newcanceluser int,
allproductorder int
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:PLATFORMID,
indexvalue:DTIME,
indexvalue:ACTIVEUSERS,
indexvalue:DAYACTIVEUSERS,
indexvalue:REGISTERUSERACTIVE,
indexvalue:REGISTERUSERACTIVERATE,
indexvalue:WATCHUSERS,
indexvalue:TOTALREGISTERUSER,
indexvalue:TOTALVISITOR,
indexvalue:ALLREGISTERUSER,
indexvalue:NEWREGISTERUSER,
indexvalue:NEWCANCELUSER,
indexvalue:ALLPRODUCTORDER
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_user_active_d");
grant select,delete,insert on t_video_develop_user_active_d_hbase to user mapred;

DROP TABLE video.t_video_develop_user_active_m_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_user_active_m_hbase
(
key string,
projectid string,
activitychannelid string,
platformid string,
mtime string,
activeusers int,
dayactiveusers int,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:PLATFORMID,
indexvalue:MTIME,
indexvalue:ACTIVEUSERS,
indexvalue:DAYACTIVEUSERS,
indexvalue:REGISTERUSERACTIVE,
indexvalue:REGISTERUSERACTIVERATE,
indexvalue:WATCHUSERS,
indexvalue:TOTALREGISTERUSER,
indexvalue:TOTALVISITOR
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_user_active_m");
grant select,delete,insert on t_video_develop_user_active_m_hbase to user mapred;

DROP TABLE video.t_video_develop_user_active_sd_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_user_active_sd_hbase
(
key string,
projectid string,
activitychannelid string,
platformid string,
dtime string,
activeusers int,
dayactiveusers int,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
newregisteruser int,
newcanceluser int
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:PLATFORMID,
indexvalue:DTIME,
indexvalue:ACTIVEUSERS,
indexvalue:DAYACTIVEUSERS,
indexvalue:REGISTERUSERACTIVE,
indexvalue:REGISTERUSERACTIVERATE,
indexvalue:WATCHUSERS,
indexvalue:TOTALREGISTERUSER,
indexvalue:TOTALVISITOR,
indexvalue:NEWREGISTERUSER,
indexvalue:NEWCANCELUSER
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_user_active_sd");
grant select,delete,insert on t_video_develop_user_active_sd_hbase to user mapred;

DROP TABLE video.t_video_develop_user_active_td_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_user_active_td_hbase
(
key string,
projectid string,
activitychannelid string,
platformid string,
dtime string,
activeusers int,
dayactiveusers int,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
newregisteruser int,
newcanceluser int
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:PLATFORMID,
indexvalue:DTIME,
indexvalue:ACTIVEUSERS,
indexvalue:DAYACTIVEUSERS,
indexvalue:REGISTERUSERACTIVE,
indexvalue:REGISTERUSERACTIVERATE,
indexvalue:WATCHUSERS,
indexvalue:TOTALREGISTERUSER,
indexvalue:TOTALVISITOR,
indexvalue:NEWREGISTERUSER,
indexvalue:NEWCANCELUSER
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_user_active_td");
grant select,delete,insert on t_video_develop_user_active_td_hbase to user mapred;

DROP TABLE video.t_video_develop_user_active_w_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_user_active_w_hbase
(
key string,
projectid string,
activitychannelid string,
platformid string,
wtime string,
activeusers int,
dayactiveusers int,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:PLATFORMID,
indexvalue:WTIME,
indexvalue:ACTIVEUSERS,
indexvalue:DAYACTIVEUSERS,
indexvalue:REGISTERUSERACTIVE,
indexvalue:REGISTERUSERACTIVERATE,
indexvalue:WATCHUSERS,
indexvalue:TOTALREGISTERUSER,
indexvalue:TOTALVISITOR
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_user_active_w");
grant select,delete,insert on t_video_develop_user_active_w_hbase to user mapred;

DROP TABLE video.t_video_users_retention_d_hbase;
CREATE EXTERNAL TABLE video.t_video_users_retention_d_hbase
(
key string,
projectid string,
activitychannelid string,
activityid string,
dtime string,
novisitorconvert int,
newvisitor int,
day01againloginregister int,
day03againloginregister int,
day07againloginregister int,
day15againloginregister int,
day30againloginregister int,
day01againloginvisitor int,
day03againloginvisitor int,
day07againloginvisitor int,
day15againloginvisitor int,
day30againloginvisitor int,
newsubscriber int,
day01retentionrate double,
day03retentionrate double,
day07retentionrate double,
day15retentionrate double,
day30retentionrate double,
avgday01retentionrate double,
avgday03retentionrate double,
avgday07retentionrate double,
avgday15retentionrate double,
avgday30retentionrate double
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:ACTIVITYID,
indexvalue:DTIME,
indexvalue:NOVISITORCONVERT,
indexvalue:NEWVISITOR,
indexvalue:DAY01AGAINLOGINREGISTER,
indexvalue:DAY03AGAINLOGINREGISTER,
indexvalue:DAY07AGAINLOGINREGISTER,
indexvalue:DAY15AGAINLOGINREGISTER,
indexvalue:DAY30AGAINLOGINREGISTER,
indexvalue:DAY01AGAINLOGINVISITOR,
indexvalue:DAY03AGAINLOGINVISITOR,
indexvalue:DAY07AGAINLOGINVISITOR,
indexvalue:DAY15AGAINLOGINVISITOR,
indexvalue:DAY30AGAINLOGINVISITOR,
indexvalue:NEWSUBSCRIBER,
indexvalue:DAY01RETENTIONRATE,
indexvalue:DAY03RETENTIONRATE,
indexvalue:DAY07RETENTIONRATE,
indexvalue:DAY15RETENTIONRATE,
indexvalue:DAY30RETENTIONRATE,
indexvalue:AVGDAY01RETENTIONRATE,
indexvalue:AVGDAY03RETENTIONRATE,
indexvalue:AVGDAY07RETENTIONRATE,
indexvalue:AVGDAY15RETENTIONRATE,
indexvalue:AVGDAY30RETENTIONRATE
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_users_retention_d");
grant select,delete,insert on t_video_users_retention_d_hbase to user mapred;
  
!quit
EOF