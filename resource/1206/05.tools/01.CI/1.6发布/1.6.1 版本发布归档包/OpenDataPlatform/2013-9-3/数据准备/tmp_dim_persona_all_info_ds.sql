#-----------------------------------------
# 开放联盟1.5联调数据
#-----------------------------------------

hive -e "
USE default;

# 上个版本的device_id列表
create table if not EXISTS tmp_alliance1dot4_device_id_ds  AS
SELECT
    device_id
FROM
    dim_persona_all_info_ds
WHERE pt_w = '20130722-20130728';

ADD jar /home/BIProd/datamining/udflib/huawei_udf.jar;
CREATE TEMPORARY FUNCTION RevertDeviceId AS 'com.huawei.udf.RevertDeviceId';

# 联盟1.5的标签宽表
CREATE EXTERNAL TABLE IF NOT EXISTS tmp_alliance1dot5_all_info_ds
(
    device_id                   STRING,
    gender                      INT    COMMENT '0:male,1:female',
    location_id                 STRING COMMENT 'geo area id',
    terminal_type               STRING COMMENT 'eg u9500(huawei string has been filtered)',
    terminal_os_ver             STRING COMMENT 'os ver eg android 4.0',
    sim_mobile_oper             STRING COMMENT 'eg China Mobile,China Unicom',
    new_user_flag               INT    COMMENT '1:new user,0:old user',
    last_state                  INT    COMMENT 'lifecycle last state',
    lat_fond_app1               STRING COMMENT 'latitude fond app 1',
    lat_fond_app2               STRING COMMENT 'latitude fond app 2',
    lat_fond_app3               STRING COMMENT 'latitude fond app 3',
    lat_fond_score1             INT    COMMENT 'lat_app1 fondness score1',
    lat_fond_score2             INT    COMMENT 'lat_app2 fondness score2',
    lat_fond_score3             INT    COMMENT 'lat_app3 fondness score3',
    game_fond_app1              STRING COMMENT 'fond game app 1 ',
    game_fond_app2              STRING COMMENT 'fond game app 2 ',
    game_fond_app3              STRING COMMENT 'fond game app 3 ',
    game_app1_score1            INT    COMMENT 'fond game app 1 score',
    game_app2_score2            INT    COMMENT 'fond game app 2 score',
    game_app3_score3            INT    COMMENT 'fond game app 3 score',
    push_login_days             INT    COMMENT 'login days via push ',
    hispace_login_days          INT    COMMENT 'login days via hispace',
    hispace_download_times      INT    COMMENT 'app download_times via hispace',
    ad_type_flag                STRING COMMENT '1: can be pushed ,0: can not',
    rom_version                 STRING,
    push_version                STRING,
    app_channel                 STRING COMMENT 'undetermind',
    user_ad_flag                TINYINT COMMENT '0:common user,1:white list,2:red list',
    hispace_type_flag           STRING COMMENT 'ph_y :can be pushed with hispace message',
    push_last_week_login_days   INT,
    hispace_version             STRING  COMMENT 'hicloud client version',
    age                         INT,
    app_package_name_list       STRING,
    screen_resolution           STRING,
    defined_interest            STRING
)
STORED AS RCFILE
LOCATION '/AppData/BIProd/datamining/tmp/tmp_alliance1dot5_all_info_ds';

INSERT OVERWRITE TABLE tmp_alliance1dot5_all_info_ds
SELECT
    n.device_id,
    max(n.gender),
    max(n.location_id),
    max(n.terminal_type),
    max(n.terminal_os_ver),
    max(n.sim_mobile_oper),
    max(n.new_user_flag),
    max(n.last_state),
    max(n.lat_fond_app1),
    max(n.lat_fond_app2),              
    max(n.lat_fond_app3),              
    max(n.lat_fond_score1),             
    max(n.lat_fond_score2),            
    max(n.lat_fond_score3),            
    max(n.game_fond_app1),              
    max(n.game_fond_app2),              
    max(n.game_fond_app3),              
    max(n.game_app1_score1),            
    max(n.game_app2_score2),            
    max(n.game_app3_score3),            
    max(n.push_login_days),             
    max(n.hispace_login_days),
    max(n.hispace_download_times),      
    max(n.ad_type_flag),     
    max(n.rom_version),               
    max(n.push_version),                
    max(n.app_channel),               
    max(n.user_ad_flag),                
    max(n.hispace_type_flag),           
    max(n.push_last_week_login_days),
    max(n.hispace_version),
    max(n.age),
    max(n.app_package_name_list),
    max(n.screen_resolution),
    max(n.defined_interest)
FROM
(
SELECT
    t2.device_id,
    t2.gender,
    t2.location_id,
    t2.terminal_type,
    t2.terminal_os_ver,
    t2.sim_mobile_oper,
    t2.new_user_flag,
    t2.last_state,
    t2.lat_fond_app1,
    t2.lat_fond_app2,              
    t2.lat_fond_app3,              
    t2.lat_fond_score1,             
    t2.lat_fond_score2,            
    t2.lat_fond_score3,            
    t2.game_fond_app1,              
    t2.game_fond_app2,              
    t2.game_fond_app3,              
    t2.game_app1_score1,            
    t2.game_app2_score2,            
    t2.game_app3_score3,            
    t2.push_login_days,             
    t2.hispace_login_days,
    t2.hispace_download_times,      
    t2.ad_type_flag,     
    t2.rom_version,               
    t2.push_version,                
    t2.app_channel,               
    t2.user_ad_flag,                
    t2.hispace_type_flag,           
    t2.push_last_week_login_days,
    t2.hispace_version,
    t0.age,
    t1.app_list AS app_package_name_list,
    t3.screen_resolution AS   screen_resolution,
    t4.defined_interest
FROM
(
    SELECT
        device_id,
        CAST ('2013' AS INT)-CAST(substr(birthday,1,4) AS INT)  AS age
    FROM
        dim_persona_up_userinfo_ds
    WHERE  birthday is not null and birthday <> ''
) t0
FULL OUTER JOIN
(
    SELECT device_id,app_list
    FROM dim_persona_hispace_applist_ds
) t1
ON (t0.device_id = t1.device_id)
RIGHT OUTER JOIN
    (
        SELECT device_id,gender,location_id,terminal_type,terminal_os_ver,sim_mobile_oper,new_user_flag,last_state,lat_fond_app1,lat_fond_app2,lat_fond_app3,lat_fond_score1,lat_fond_score2,lat_fond_score3,game_fond_app1,game_fond_app2,game_fond_app3,game_app1_score1,game_app2_score2,game_app3_score3,push_login_days,hispace_login_days,hispace_download_times,ad_type_flag,rom_version,push_version,app_channel,user_ad_flag,hispace_type_flag,push_last_week_login_days,hispace_version from dim_persona_all_info_ds where pt_w = '20130812-20130818'
    ) t2
ON (t0.device_id = t2.device_id)
LEFT OUTER JOIN
(
    SELECT
        device_name,
        screen_resolution
    FROM
        dim_persona_deviceinfo_ds
) t3
ON (lower(t2.terminal_type) = lower(t3.device_name))
LEFT OUTER JOIN
(
    SELECT
        RevertDeviceId(device_id)       AS      device_id,
        labels                          AS      defined_interest
    FROM
        dw_user_defined_label_ds
) t4
ON (t2.device_id = t4.device_id)
) n
GROUP BY n.device_id;


"