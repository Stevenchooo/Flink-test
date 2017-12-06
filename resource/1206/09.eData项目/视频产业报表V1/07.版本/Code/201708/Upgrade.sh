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
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day_newlogin_visitor to user mapred;

drop table video.t_mid_day_notconvert_registeredusers;
create table video.t_mid_day_notconvert_registeredusers
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
identityid         string
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day_notconvert_registeredusers to user mapred;

drop table video.t_mid_day01againloginregister_tmp;
create table video.t_mid_day01againloginregister_tmp
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
day01againloginregister      int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day01againloginregister_tmp to user mapred;

drop table video.t_mid_day03againloginregister_tmp;
create table video.t_mid_day03againloginregister_tmp
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
day03againloginregister      int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day03againloginregister_tmp to user mapred;

drop table video.t_mid_day07againloginregister_tmp;
create table video.t_mid_day07againloginregister_tmp
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
day07againloginregister      int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day07againloginregister_tmp to user mapred;

drop table video.t_mid_day15againloginregister_tmp;
create table video.t_mid_day15againloginregister_tmp
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
day15againloginregister      int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day15againloginregister_tmp to user mapred;

drop table video.t_mid_day30againloginregister_tmp;
create table video.t_mid_day30againloginregister_tmp
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
day30againloginregister      int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_day30againloginregister_tmp to user mapred;

drop table video.t_mid_newsubscriber_tmp;
create table video.t_mid_newsubscriber_tmp
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
newsubscriber      int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_newsubscriber_tmp to user mapred;

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
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_activeusers_dw to user mapred;

drop table video.t_mid_video_sdk_appdownload_d;
create table video.t_mid_video_sdk_appdownload_d
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
appdownload int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_appdownload_d to user mapred;

drop table video.t_mid_video_sdk_appdownload_dm;
create table video.t_mid_video_sdk_appdownload_dm
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
appdownload int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_appdownload_dm to user mapred;

drop table video.t_mid_video_sdk_appdownload_dw;
create table video.t_mid_video_sdk_appdownload_dw
(
projectid string,
platformid string,
activitychannelid string,
activityid  string,
dtime string,
appdownload int
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_appdownload_dw to user mapred;

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
loginuser int,
appdownload int ,
newsubscriber int ,
day01retentionrate double,
day03retentionrate double,
day07retentionrate double,
day15retentionrate double,
day30retentionrate double
)
partitioned by(
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
loginuser int,
appdownload int
)
partitioned by(
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
loginuser int,
appdownload int
)
partitioned by(
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
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_reguseractive_dw to user mapred;

drop table video.t_mid_video_sdk_retentionrate_d;
create table video.t_mid_video_sdk_retentionrate_d
(
projectid          string,
platformid         string,
activitychannelid  string,
activityid         string,
dtime              string,
newsubscriber           int,
day01retentionrate      double,
day03retentionrate      double,
day07retentionrate      double,
day15retentionrate      double,
day30retentionrate      double
)
partitioned by(
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on video.t_mid_video_sdk_retentionrate_d to user mapred;

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
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_totalvisitor_dm to user mapred;

drop table video.t_mid_video_sdk_totalvisitor_dw;
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
date string
)
row format delimited fields terminated by '\t' map keys terminated by '&' stored as textfile;
grant select,delete,insert on t_mid_video_sdk_totalvisitor_dw to user mapred;

DROP TABLE video.t_video_2BData_data_d_hbase;
CREATE EXTERNAL TABLE video.t_video_2BData_data_d_hbase
(
key                     string,
projectid               string,
dtime                   string,
newvisitor              int,
activeusers             int,
totalvisitor            int,
registeruseractive      int,
activeusers_m           int,
registeruseractive_m    int,
newregisteruser         int,
totalregisteruser       int,
totalactiveaccount      int,
newpay                  int,
pay                     int,
pay_m                   int,
paytotal                int,
newincome               double,
arpuincome              double,
videosnumber            int,
playtimes               int,
playusers               int,
playtime                int,
percapitaplaytime       double,
percapitaplay           double,
avgvideotime            double,
devicearpu              double,
accountarpu             double
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:DTIME,
indexvalue:NEWVISITOR,
indexvalue:ACTIVEUSERS,
indexvalue:TOTALVISITOR,
indexvalue:REGISTERUSERACTIVE,
indexvalue:ACTIVEUSERS_M,
indexvalue:REGISTERUSERACTIVE_M,
indexvalue:NEWREGISTERUSER,
indexvalue:TOTALREGISTERUSER,
indexvalue:TOTALACTIVEACCOUNT,
indexvalue:NEWPAY,
indexvalue:PAY,
indexvalue:PAY_M,
indexvalue:PAYTOTAL,
indexvalue:NEWINCOME,
indexvalue:ARPUINCOME,
indexvalue:VIDEOSNUMBER,
indexvalue:PLAYTIMES,
indexvalue:PLAYUSERS,
indexvalue:PLAYTIME,
indexvalue:PERCAPITAPLAYTIME,
indexvalue:PERCAPITAPLAY,
indexvalue:AVGVIDEOTIME,
indexvalue:DEVICEARPU,
indexvalue:ACCOUNTARPU
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_2BData_data_d");
grant select,delete,insert on video.t_video_2BData_data_d_hbase to user mapred;

DROP TABLE video.t_video_develop_use_active_appversion_d_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_use_active_appversion_d_hbase
(
key string,
projectid int,
platformid string,
appversioncode string,
appvernum int,
appverpercent double,
dtime string
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,
indexvalue:PROJECTID,
indexvalue:PLATFORMID,
indexvalue:APPVERSIONCODE,
indexvalue:APPVERNUM,
indexvalue:APPVERPERCENT,
indexvalue:DTIME
")
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_use_active_appversion_d");
grant select,delete,insert on t_video_develop_use_active_appversion_d_hbase to user mapred;

DROP TABLE video.t_video_develop_user_active_d_hbase;
CREATE EXTERNAL TABLE video.t_video_develop_user_active_d_hbase
(
key string,
projectid string,
activitychannelid string,
platformid string,
dtime string,
activeusers int,
dayactiveusers double,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
allregisteruser int,
newregisteruser int,
newcanceluser int,
allproductorder int,
appdownload int
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
indexvalue:ALLPRODUCTORDER,
indexvalue:APPDOWNLOAD
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
dayactiveusers double,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
appdownload int
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
indexvalue:TOTALVISITOR,
indexvalue:APPDOWNLOAD
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
dayactiveusers double,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
newregisteruser int,
newcanceluser int,
appdownload int
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
indexvalue:NEWCANCELUSER,
indexvalue:APPDOWNLOAD
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
dayactiveusers double,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
newregisteruser int,
newcanceluser int,
appdownload int
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
indexvalue:NEWCANCELUSER,
indexvalue:APPDOWNLOAD
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
dayactiveusers double,
registeruseractive int,
registeruseractiverate double,
watchusers int,
totalregisteruser int,
totalvisitor int,
appdownload int
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
indexvalue:TOTALVISITOR,
indexvalue:APPDOWNLOAD
") 
TBLPROPERTIES ("hbase.table.name" = "t_video_develop_user_active_w");
grant select,delete,insert on t_video_develop_user_active_w_hbase to user mapred;

DROP TABLE video.t_video_users_retention_d_hbase;
CREATE EXTERNAL TABLE video.t_video_users_retention_d_hbase
(
key string,
projectid string,
platformid string,
activitychannelid string,
activityid string,
dtime string,
newsubscriber      int,
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
indexvalue:PLATFORMID,
indexvalue:ACTIVITYCHANNELID,
indexvalue:ACTIVITYID,
indexvalue:DTIME,
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
grant select,delete,insert on video.t_video_users_retention_d_hbase to user mapred;
  
!quit
EOF