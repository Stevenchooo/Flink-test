CREATE TEMPORARY FUNCTION udf_quota as 'com.huawei.bi.hive_udf.udf_quota';

set hive.cli.print.header=true;
 
insert overwrite local directory '${env:fdPath}'
SELECT
*
FROM
(
SELECT
    t0.rowkey,
    t0.gender,
    t0.sim_mobile_oper,
    t0.hispace_login_days,
    t0.hispace_download_times,
    t0.push_login_days,
    t0.geo_province,
    t0.game_interest,
    t0.lat_interest,
    t0.life_last_state,
    t0.new_flag,
    t0.terminal,
    t0.os,
    t0.user_ad_flag,
    t0.ad_type_flag,
    t0.hispace_type_flag,
    t0.push_last_week_login_days,
    t0.push_version,
    t0.app_channel,
    t0.rom_version,
    t0.hispace_version,
    t0.age,
    t0.app_list,
    t0.screen_resolution,
    t0.defined_interest,
    t0.push_login_time,
    t0.hispace_life_days
FROM
(
select
--
-- 1. column name requirements:
--    must be composed from alphanumeric characters plus underscore, parentheses, brackets.
--    must start with an alphabet or a underscore.
--    names are case-insensitive.
-- 2. String values containing blank spaces must quoted because blank space is assumed to be delimiter as well.
--
--    udf_quota(concat(udf_sum_mod(device_id, 8), '-', device_id)) rowkey,
    udf_quota(n.device_id) rowkey,

    udf_quota(
    CASE n.gender
         WHEN 0 THEN 'M'
         WHEN 1 THEN 'F'
         ELSE 'X'
    END) gender,

    udf_quota(
        n.sim_mobile_oper
    ) sim_mobile_oper,

    udf_quota(
    IF (n.hispace_login_days is null, '0',
    IF (n.hispace_login_days >= 1 and n.hispace_login_days <= 5, '1_5',
    if (n.hispace_login_days <= 10, '6_10',
    if (n.hispace_login_days <= 20, '11_20',
    if (n.hispace_login_days <= 30, '21_30',
    if (n.hispace_login_days <= 60, '31_60',
    if (n.hispace_login_days > 60, '60+','0')))))))) hispace_login_days,

    udf_quota(
    IF(n.hispace_download_times is null, '0',
    IF(n.hispace_download_times = 0,'0',
    IF(n.hispace_download_times <= 5,'1_5',
    IF(n.hispace_download_times <= 15,'6_15',
    IF(n.hispace_download_times <= 30,'16_30',
    IF(n.hispace_download_times >30,'30+','0'))))))) hispace_download_times,

    udf_quota(
    IF (n.push_login_days is null, '0',
    IF (n.push_login_days < 1, '0',
    IF (n.push_login_days <= 5, '1_5',
    if (n.push_login_days <= 10, '6_10',
    if (n.push_login_days <= 15, '11_15','15+')))))) push_login_days,

    udf_quota(
    if(n.location_id = -1 OR n.location_id is NULL, 'X',n.location_id)
    ) geo_province,

    udf_quota(
    concat(
    if (n.game_fond_app1 is null, '',
    if (n.game_app1_score1 is null or n.game_app1_score1 <= 0, '', n.game_fond_app1)),
    '\003',
    if (n.game_fond_app2 is null, '',
    if (n.game_app2_score2 is null or n.game_app2_score2 <= 0, '', n.game_fond_app2)),
    '\003',
    if (n.game_fond_app3 is null, '',
    if (n.game_app3_score3 is null or n.game_app3_score3 <= 0, '', n.game_fond_app3))
       )
    ) game_interest,

    udf_quota(
    concat(
    if (n.lat_fond_app1 is null, '',
    if (n.lat_fond_score1 is null or n.lat_fond_score1 <= 0, '', n.lat_fond_app1)),
    '\003',
    if (n.lat_fond_app2 is null, '',
    if (n.lat_fond_score2 is null or n.lat_fond_score2 <= 0, '', n.lat_fond_app2)),
    '\003',
    if (n.lat_fond_app3 is null, '',
    if (n.lat_fond_score3 is null or n.lat_fond_score3 <= 0, '', n.lat_fond_app3))
       )
    ) lat_interest,

    udf_quota(
    CASE n.last_state
        WHEN 0 THEN 'silent'
        WHEN 1 THEN 'minor'
        WHEN 2 THEN 'major'
        WHEN 3 THEN 'grow'
        WHEN 4 THEN 'shrink'
        ELSE 'X'
    END) life_last_state,

    udf_quota(
    CASE n.hispace_new_user_flag
        WHEN 1 THEN 'new'
        WHEN 0 THEN 'old'
        ELSE 'X'
    END
    ) new_flag,

    udf_quota(
    CASE n.device_name
        WHEN '-1' THEN 'X'
        WHEN ''   THEN 'X'
        ELSE IF (isnotnull(n.device_name),
                        regexp_replace(n.device_name, '\\^|-', '_'),
                'X')
    END) terminal,

    udf_quota(
    CASE substr(n.os_ver,1,3)
        WHEN '1.5' THEN '1.5'
        WHEN '2.1' THEN '2.1'
        WHEN '2.2' THEN '2.2'
        WHEN '2.3' THEN '2.3'
        WHEN '3.2' THEN '3.2'
        WHEN '4.0' THEN '4.0'
        WHEN '4.1' THEN '4.1'
        WHEN '4.2' THEN '4.2'
        ELSE 'X'
    END
    ) os,

    udf_quota(
    CASE cast(n.user_ad_flag AS int)
         WHEN 0 THEN '0'
         WHEN 1 THEN '1'
         WHEN 2 THEN '2'
         ELSE 'X'
    END) user_ad_flag,

    udf_quota(
    CASE cast(n.ad_type_flag AS int)
         WHEN 1 THEN '1'
         WHEN 0 THEN '0'
         ELSE 'X'
    END) ad_type_flag,

    udf_quota(
    if (n.hispace_type_flag = 'ph_y' or push_channel like '%com.huawei.appmarket%','1','0')
    ) hispace_type_flag,

    udf_quota(
    CASE n.push_last_week_login_days
         WHEN 1 THEN '1'
         WHEN 2 THEN '2'
         WHEN 3 THEN '3'
         WHEN 4 THEN '4'
         WHEN 5 THEN '5'
         WHEN 6 THEN '6'
         WHEN 7 THEN '7'
         ELSE '0'
    END
    ) push_last_week_login_days,

    udf_quota(
    IF(n.push_version is null ,'X',n.push_version)
    ) push_version,

    udf_quota(
    if(n.push_channel is null,'',regexp_replace(n.push_channel, '\043', '\003'))
    ) app_channel,

    udf_quota(
    n.rom_ver
    ) rom_version,

    udf_quota(
    if(n.hispace_version rlike '[\\\\d\\\\.]',n.hispace_version,'X')
    ) hispace_version,

    udf_quota(
    IF (n.age is null ,'X',
    IF (n.age >= 0 AND n.age <= 12,'0_12',
    IF (n.age <=17,'13_17',
    IF (n.age <=25,'18_25',
    IF (n.age <=35,'26_35',
    IF (n.age <=45,'36_45',
    IF (n.age <=60,'46_60',
    IF (n.age <= 200,'60+','X'))))))))
    ) age,

    udf_quota(
    IF (n.app_list is null, '',regexp_replace(n.app_list,'\043','\003'))
    ) app_list,

    udf_quota(
    IF (length(substr(n.screen_resolution,1,locate('*',n.screen_resolution)-1)) <= 4 AND length(substr(n.screen_resolution,1,locate('*',n.screen_resolution)-1)) > 0,n.screen_resolution,'X')
    ) screen_resolution,

    udf_quota(
    IF (n.defined_interest is null ,'',regexp_replace(n.defined_interest,',','\003'))
    ) defined_interest,
    
    udf_quota(
    if  (n.push_login_time <= 7, '1w',
    if  (n.push_login_time <= 14, '1+_2w',
    if  (n.push_login_time <= 21, '2+_3w',
    if  (n.push_login_time <= 28, '3+_4w',
    if  (n.push_login_time >= 29 and n.push_login_time <= 30, '4w_1m',
    if  (n.push_login_time <= 60, '1m+_2m',
    if  (n.push_login_time <= 90, '2m+_3m','3m+')))))))
    ) push_login_time,

    udf_quota(
        IF ( hispace_life_days IS NULL,'X'
        IF ( hispace_life_days >= 1 AND life_days <= 7, concat('hi_life_',CAST (life_days as STRING)),
        IF ( hispace_life_days <= 14, 'hi_life_8_14',
        IF ( hispace_life_days = 15, 'hi_life_15',
        IF ( hispace_life_days <= 19, 'hi_life_16_19',
        IF ( hispace_life_days < 30,'hi_life_20_29',
        IF ( hispace_life_days = 30,'hi_life_30',
        IF ( hispace_life_days <= 59,'hi_life_31_59',
        IF ( hispace_life_days = 60,'hi_life_60',
        IF ( hispace_life_days <= 89,'hi_life_61_89',
        IF ( hispace_life_days = 90,'hi_life_90',
        IF ( hispace_life_days <= 364,'hi_life_91_364',
        IF ( hispace_life_days = 365,'hi_life_365','365+')))))))))))))
    ) hispace_life_days

FROM 
   (
        SELECT
            tt2.device_id       as  device_id,
            tt2.device_name     as  device_name,
            tt2.os_ver          as  os_ver,
            tt2.rom_ver         as  rom_ver,
            IF ( tt2.plmn is NULL or plmn = '', 'X',
            IF ( tt2.plmn = '46000', 'China Mobile',
            IF ( tt2.plmn = '46002', 'China Mobile',
            IF ( tt2.plmn = '46007', 'China Mobile',
            IF ( tt2.plmn = '46001', 'China Unicom',
            IF ( tt2.plmn = '46006', 'China Unicom',
            IF ( tt2.plmn = '46003', 'China Telecom',
            IF ( tt2.plmn = '46005', 'China Telecom','plmn_else'))))))))    AS      sim_mobile_oper,
            tt2.gender,
            CAST(substr('${env:CURRENT_DATE}',1,4) AS INT)-CAST(substr(tt2.birthday,1,4) AS INT )      AS      age,
            tt2.hispace_login_days          as    hispace_login_days,
            tt2.hispace_download_times      as  hispace_download_times,
            tt2.push_login_days             as  push_login_days,
            COALESCE(tt1.parent_area_id,tt2.loc_id_longterm)            AS  location_id,
            tt2.game_fond_app1              as  game_fond_app1,
            tt2.game_fond_app2              as  game_fond_app2,
            tt2.game_fond_app3              as  game_fond_app3,
            tt2.game_app1_score1            as  game_app1_score1,
            tt2.game_app2_score2            as  game_app2_score2,
            tt2.game_app3_score3            as  game_app3_score3,
            tt2.lat_fond_app1               as  lat_fond_app1,
            tt2.lat_fond_app2               as  lat_fond_app2,
            tt2.lat_fond_app3               as  lat_fond_app3,
            tt2.lat_fond_score1             as  lat_fond_score1,
            tt2.lat_fond_score2             as  lat_fond_score2,
            tt2.lat_fond_score3             as  lat_fond_score3 ,
            tt2.hispace_new_user_flag       as  hispace_new_user_flag,
            tt2.push_last_week_login_days   as  push_last_week_login_days,
            tt2.push_avg_online_duration    as  push_avg_online_duration,
            tt2.last_state                  as  last_state,
            tt2.push_channel                as  push_channel,
            tt2.push_version                as  push_version,
            tt2.screen_resolution           as  screen_resolution,
            tt2.app_list                    as  app_list,
            tt2.ad_type_flag                as  ad_type_flag,
            tt2.user_ad_flag                as  user_ad_flag,
            tt2.hispace_type_flag           as  hispace_type_flag,
            tt2.hispace_version             as  hispace_version,
            tt2.defined_interest            as  defined_interest,
            tt2.push_login_time             as  push_login_time,
            IF( tt2.biz_code & 1 <> 0,
                datediff('${env:CURRENT_DATE_EP}',substr(create_time,1,10)),NULL
                )    as      hispace_life_days
        FROM
            (SELECT area_id,parent_area_id FROM dim_location_cn_ds WHERE area_level = 4)tt1 
        RIGHT OUTER JOIN
            (select * from dim_persona_all_info_ds where hispace_type_flag is not null or push_channel is not null or ad_type_flag is not null and pt_d = ${env:CURRENT_DATE}) tt2
        ON (tt1.area_id = tt2.loc_id_longterm)
   ) n
) t0
) m order by m.rowkey;


