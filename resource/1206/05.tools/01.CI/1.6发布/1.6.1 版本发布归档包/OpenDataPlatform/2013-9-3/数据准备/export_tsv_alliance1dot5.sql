 source hive_set.sql;
 source hive_udf_decl.sql;

 set mapred.task.timeout=0;
 set mapred.map.tasks=64;
 set hive.exec.compress.output=false;
 set hive.cli.print.header=true;

insert overwrite local directory 'tsv_ubs/hive.exp_alliance1dot5'
-- extra head sql start --
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
    t0.defined_interest
FROM
(
-- extra head sql end --
select
--
-- 1. column name requirements:
--    must be composed from alphanumeric characters plus underscore, parentheses, brackets.
--    must start with an alphabet or a underscore.
--    names are case-insensitive.
-- 2. String values containing blank spaces must quoted because blank space is assumed to be delimiter as w     ell.
--
--    udf_quota(concat(udf_sum_mod(device_id, 8), "-", device_id)) rowkey,
      udf_quota(device_id) rowkey,

    udf_quota(
    CASE gender
         WHEN 0 THEN "M"
         WHEN 1 THEN "F"
         WHEN -1 THEN "X"
         ELSE NULL
    END) gender,

    udf_quota(
    CASE sim_mobile_oper
         WHEN "China Mobile" THEN sim_mobile_oper
         WHEN "China Tietong" THEN "China Mobile"
         WHEN "China Unicom" THEN sim_mobile_oper
         WHEN "China Telecom" THEN sim_mobile_oper
         ELSE NULL
    END) sim_mobile_oper,

--     udf_quota(
--     CASE big_file_download_flag
--          WHEN 0 THEN "0_50MB"
--          WHEN 1 THEN "50_100MB"
--          WHEN 2 THEN "100MB+"
--          ELSE NULL
--     END) big_file_download_flag,
--
--     udf_quota(
--     CASE 3g_2g_big_file_download_flag
--          WHEN 0 THEN "0_50MB"
--          WHEN 1 THEN "50_100MB"
--          WHEN 2 THEN "100MB+"
--          ELSE NULL
--     END) 3g_2g_big_file_download_flag,
--
--     udf_quota(
--     CASE hispace_dormant_flag
--          WHEN 1 THEN "1stweek_new"
--          WHEN 2 THEN "1stmonth_new"
--          WHEN 4 THEN "2month_old"
--          WHEN 8 THEN "1month_old"
--          WHEN 12 THEN "1month_old"
--          ELSE NULL
--     END) hispace_dormant_flag,
--
    udf_quota(
    IF (hispace_login_days is null, null,
    IF (hispace_login_days >= 1 and hispace_login_days <= 5, "1_5",
    if (hispace_login_days <= 10, "6_10",
    if (hispace_login_days <= 20, "11_20",
    if (hispace_login_days > 20, "20+",NULL)))))) hispace_login_days,

    udf_quota(
    IF(hispace_download_times is null, null,
    IF(hispace_download_times = 0,"zero",
    IF(hispace_download_times <= 5,"1_5",
    IF(hispace_download_times <= 15,"6_15",
    IF(hispace_download_times <= 30,"16_30",
    IF(hispace_download_times>30,"30+",NULL))))))) hispace_download_times,

--     udf_quota(
--     CASE hispace_revenue_level
--          WHEN 0 THEN "0"
--          WHEN 1 THEN "1"
--          WHEN 2 THEN "2"
--          ELSE NULL
--     END) hispace_revenue_level,

--     udf_quota(
--     CASE wifi_flag
--          WHEN 1 THEN "wifi"
--          ELSE NULL
--     END) wifi_flag,
--
--     udf_quota(
--     CASE 2g_flag
--          WHEN 1 THEN "x2g"
--          ELSE NULL
--     END) x2g_flag,
--
--     udf_quota(
--     CASE 3g_flag
--          WHEN 1 THEN "x3g"
--          ELSE NULL
--     END) x3g_flag,

    udf_quota(
    IF (push_login_days is null, null,
    IF (push_login_days < 1, null,
    IF (push_login_days <= 5, "1_5",
    if (push_login_days <= 10, "6_10",
    if (push_login_days <= 15, "11_15","15+")))))) push_login_days,

--     udf_quota(
--     IF(push_avg_online_duration is null,null,
--     if(push_avg_online_duration < 1,null,
--     if(push_avg_online_duration < 3600,"1_3600",
--     if(push_avg_online_duration < 21600,"3600_21600",
--     if(push_avg_online_duration < 43200,"21600_43200",
--        "x43200_86400")))))) push_avg_online_duration,


--     udf_quota(
--     if (push_op_1 is null, null,
--     if (push_op_1_cnts is null or push_op_1_cnts <= 0, null, push_op_1))) push_most_period1,
--     udf_quota(
--     if (push_op_2 is null, null,
--     if (push_op_2_cnts is null or push_op_2_cnts <= 0, null, push_op_2))) push_most_period2,
--     udf_quota(
--     if (push_op_3 is null, null,
--     if (push_op_3_cnts is null or push_op_3_cnts <= 0, null, push_op_3))) push_most_period3,

    udf_quota(
    location_id
    ) geo_province,

    udf_quota(
    concat(
    if (game_fond_app1 is null, "",
    if (game_app1_score1 is null or game_app1_score1 <= 0, "", game_fond_app1)),
    '\003',
    if (game_fond_app2 is null, "",
    if (game_app2_score2 is null or game_app2_score2 <= 0, "", game_fond_app2)),
    '\003',
    if (game_fond_app3 is null, "",
    if (game_app3_score3 is null or game_app3_score3 <= 0, "", game_fond_app3))
       )
    ) game_interest,
--     udf_quota(
--     if (game_fond_app2 is null, null,
--     if (game_app2_score2 is null or game_app2_score2 <= 0, null, game_fond_app2))) game_interest2,
--     udf_quota(
--     if (game_fond_app3 is null, null,
--     if (game_app3_score3 is null or game_app3_score3 <= 0, null, game_fond_app3))) game_interest3,

    udf_quota(
    concat(
    if (lat_fond_app1 is null, "",
    if (lat_fond_score1 is null or lat_fond_score1 <= 0, "", lat_fond_app1)),
    '\003',
    if (lat_fond_app2 is null, "",
    if (lat_fond_score2 is null or lat_fond_score2 <= 0, "", lat_fond_app2)),
    '\003',
    if (lat_fond_app3 is null, "",
    if (lat_fond_score3 is null or lat_fond_score3 <= 0, "", lat_fond_app3))
       )
    ) lat_interest,
--     udf_quota(
--     if (lat_fond_app2 is null, null,
--     if (lat_fond_score2 is null or lat_fond_score2 <= 0, null, lat_fond_app2))) lat_interest2,
--     udf_quota(
--     if (lat_fond_app3 is null, null,
--     if (lat_fond_score3 is null or lat_fond_score3 <= 0, null, lat_fond_app3))) lat_interest3,

--     udf_quota(
--     if (keyword_1 is null, null,
--     if (relevance_1 is null or relevance_1 <= 0, null, keyword_1))) keyword1,
--     udf_quota(
--     if (keyword_2 is null, null,
--     if (relevance_2 is null or relevance_2 <= 0, null, keyword_2))) keyword2,
--     udf_quota(
--     if (keyword_3 is null, null,
--     if (relevance_3 is null or relevance_3 <= 0, null, keyword_3))) keyword3,
--
--     udf_quota(
--     if (pay_times is null, null,
--     if (pay_times <= 1, "1",
--     if (pay_times = 2,"2",
--     if (pay_times <= 5, "3_5",
--     if (pay_times <= 10, "6_10",
--     if (pay_times <= 50, "11_50",  "50+"))))))) pay_times,
--
--     udf_quota(
--     if (pay_money is null, null,
--         if (pay_money <= 10, "0+_10",
--         if (pay_money <= 20, "11_20",
--         if (pay_money <= 50, "21_50",
--         if (pay_money <= 100, "51_100",
--         if (pay_money <= 200,"101_200", "200+"))))))) pay_money,
--
--     udf_quota(
--     CASE deep_pay_user
--         WHEN 1 THEN "y"
--         WHEN 0 THEN "n"
--         ELSE NULL
--     END) deep_pay_user,
--
--     udf_quota(
--     CASE pay_user
--         WHEN 1 THEN "y"
--         WHEN 0 THEN "n"
--         ELSE NULL
--     END) pay_user,

--   udf_quota(
--    if ((emui_ver is null) or (emui_ver = "-1"), null,
--    if (substr(emui_ver, 1, 13) rlike '^EmotionUI_\\d\\.\\d*', substr(emui_ver, 1, 13),'EmotionUI_else'))     ) emui_ver,

    udf_quota(
    CASE last_state
        WHEN 0 THEN "silent"
        WHEN 1 THEN "minor"
        WHEN 2 THEN "major"
        WHEN 3 THEN "grow"
        WHEN 4 THEN "shrink"
        ELSE NULL
    END) life_last_state,

    udf_quota(
    CASE new_user_flag
        WHEN 1 THEN "new"
        WHEN 0 THEN "old"
        ELSE NULL
    END) new_flag,

    udf_quota(
    CASE terminal_type
        WHEN "-1" THEN NULL
        ELSE IF (isnotnull(terminal_type),
                        regexp_replace(terminal_type, "\\^|-", "_"),
                NULL)
    END) terminal,

    udf_quota(
    CASE terminal_os_ver
        WHEN "-999" THEN NULL
        WHEN "-1" THEN NULL
        ELSE IF (isnotnull(terminal_os_ver),
                regexp_replace(terminal_os_ver, "\\^", "_"),
                NULL)
    END) os,

--     udf_quota(
--     CASE biz_code
--         WHEN 1 THEN "hispace_user"
--         WHEN 2 THEN "push_user"
--         WHEN 3 THEN "hispace_push_user"
--         ELSE NULL
--     END) biz_code

    udf_quota(
    CASE cast(user_ad_flag AS int)
         WHEN 0 THEN "0"
         WHEN 1 THEN "1"
         WHEN 2 THEN "2"
         ELSE NULL
    END) user_ad_flag,

    udf_quota(
    CASE cast(ad_type_flag AS int)
         WHEN 1 THEN "1"
         WHEN 0 THEN "0"
         ELSE NULL
    END) ad_type_flag,

    udf_quota(
    CASE hispace_type_flag
         WHEN 'ph_y' THEN '1'
         ELSE NULL
    END
    ) hispace_type_flag,

    udf_quota(
    CASE push_last_week_login_days
         WHEN 1 THEN '1'
         WHEN 2 THEN '2'
         WHEN 3 THEN '3'
         WHEN 4 THEN '4'
         WHEN 5 THEN '5'
         WHEN 6 THEN '6'
         WHEN 7 THEN '7'
         ELSE NULL
    END
    ) push_last_week_login_days,

    udf_quota(
    push_version
    ) push_version,

    udf_quota(
    if(app_channel is null,"",regexp_replace(app_channel, '\043', '\003'))
    ) app_channel,

    udf_quota(
    rom_version
    ) rom_version,

    udf_quota(
    hispace_version
    ) hispace_version,

    udf_quota(
    IF (age is null ,'x',
    IF (age >= 0 AND age <= 10,'0_10',
    IF (age <=20,'11_20',
    IF (age <=30,'21_30',
    IF (age <=40,'31_40',
    IF (age <=50,'41_50',
    IF (age <=60,'51_60',
    IF (age <=70,'61_70',
    IF (age <=80,'71_80',
    IF (age <=90,'81_90',
    IF (age <=100,'91_100','101+')))))))))))
    ) age,

    udf_quota(
    IF (app_package_name_list is null, "",regexp_replace(app_package_name_list,'\043','\003'))
    ) app_list,

    udf_quota(
    screen_resolution
    ) screen_resolution,

    udf_quota(
    IF (defined_interest is null ,"",regexp_replace(defined_interest,',','\003'))
    ) defined_interest

FROM tmp_alliance1dot5_all_info_ds WHERE device_id is not null AND device_id <> '357806042791482' and device_id <> '358313040231941' AND device_id <> '356521040775152' AND device_id <> '358313040231942' and device_id <> '358313040231943' and device_id <> '358313040231944' and device_id <> '358313040231945' and device_id <> '866415010534223' and device_id <> '357667040247780' and device_id <> '860839019963032' and device_id <> '358313040231946' and device_id <> '358313040231947' and device_id <> '358313040231948' and device_id <> '358313040231949' 
) t0
union all 
SELECT
    t1.rowkey,
    t1.gender,
    t1.sim_mobile_oper,
    t1.hispace_login_days,
    t1.hispace_download_times,
    t1.push_login_days,
    t1.geo_province,
    t1.game_interest,
    t1.lat_interest,
    t1.life_last_state,
    t1.new_flag,
    t1.terminal,
    t1.os,
    t1.user_ad_flag,
    t1.ad_type_flag,
    t1.hispace_type_flag,
    t1.push_last_week_login_days,
    t1.push_version,
    t1.app_channel,
    t1.rom_version,
    t1.hispace_version,
    t1.age,
    t1.app_list,
    t1.screen_resolution,
    t1.defined_interest
FROM
(
select
    udf_quota(357806042791482) rowkey,

    udf_quota(
     "F"
    ) gender,

    udf_quota(
         "China Mobile"
    ) sim_mobile_oper,

    udf_quota(
    "1_5"
    ) hispace_login_days,

    udf_quota(
    "zero"
    ) hispace_download_times,

    udf_quota(
    "1_5"
    ) push_login_days,

    udf_quota(
    "500040132000000"
    ) geo_province,

    udf_quota(
    concat('动作冒险','\003','角色游戏')
    ) game_interest,

    udf_quota(
    concat('影音娱乐','\003','图书阅读')
    ) lat_interest,

    udf_quota(
    "silent"
    ) life_last_state,

    udf_quota(
    "new"
    ) new_flag,

    udf_quota(
    "8022"
    ) terminal,

    udf_quota(
    "1.5"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '1'
    ) push_last_week_login_days,

    udf_quota(
    "2503"
    ) push_version,

    udf_quota(
    concat("com.huawei.android.pushagent",'\003',"com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "2.3.018.P2.120515.8022"
    ) rom_version,

    udf_quota(
    "3.8.32"
    ) hispace_version,

    udf_quota('0_10') age,
    
    udf_quota(concat('com.tencent.mm','\003','com.tencent.mobileqq')) app_list,

    udf_quota('240*320') screen_resolution,

    udf_quota(concat('白领','\003','学生')) defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t1
union All
SELECT
    t2.rowkey,
    t2.gender,
    t2.sim_mobile_oper,
    t2.hispace_login_days,
    t2.hispace_download_times,
    t2.push_login_days,
    t2.geo_province,
    t2.game_interest,
    t2.lat_interest,
    t2.life_last_state,
    t2.new_flag,
    t2.terminal,
    t2.os,
    t2.user_ad_flag,
    t2.ad_type_flag,
    t2.hispace_type_flag,
    t2.push_last_week_login_days,
    t2.push_version,
    t2.app_channel,
    t2.rom_version,
    t2.hispace_version,
    t2.age,
    t2.app_list,
    t2.screen_resolution,
    t2.defined_interest
FROM
(
select
    udf_quota(358313040231941) rowkey,

    udf_quota(
     "F"
    ) gender,

    udf_quota(
         "China Mobile"
    ) sim_mobile_oper,

    udf_quota(
    "1_5"
    ) hispace_login_days,

    udf_quota(
    "1_5"
    ) hispace_download_times,

    udf_quota(
    "1_5"
    ) push_login_days,

    udf_quota(
    "500040132000000"
    ) geo_province,

    udf_quota(
    '角色游戏'
    ) game_interest,

    udf_quota(
    '影音娱乐'
    ) lat_interest,

    udf_quota(
    "silent"
    ) life_last_state,

    udf_quota(
    "new"
    ) new_flag,

    udf_quota(
    "8022"
    ) terminal,

    udf_quota(
    "1.5"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '1'
    ) push_last_week_login_days,

    udf_quota(
    "2503"
    ) push_version,

    udf_quota(
    concat("com.huawei.android.pushagent",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "2.3.018.P2.120515.8022"
    ) rom_version,

    udf_quota(
    "3.8.32"
    ) hispace_version,
    
    udf_quota('0_10') age,
    
    udf_quota('com.tencent.mm') app_list,

    udf_quota('240*320') screen_resolution,

    udf_quota('白领') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t2

union All
SELECT
    t3.rowkey,
    t3.gender,
    t3.sim_mobile_oper,
    t3.hispace_login_days,
    t3.hispace_download_times,
    t3.push_login_days,
    t3.geo_province,
    t3.game_interest,
    t3.lat_interest,
    t3.life_last_state,
    t3.new_flag,
    t3.terminal,
    t3.os,
    t3.user_ad_flag,
    t3.ad_type_flag,
    t3.hispace_type_flag,
    t3.push_last_week_login_days,
    t3.push_version,
    t3.app_channel,
    t3.rom_version,
    t3.hispace_version,
    t3.age,
    t3.app_list,
    t3.screen_resolution,
    t3.defined_interest
FROM
(
select
    udf_quota(356521040775152) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t3

union All
SELECT
    t4.rowkey,
    t4.gender,
    t4.sim_mobile_oper,
    t4.hispace_login_days,
    t4.hispace_download_times,
    t4.push_login_days,
    t4.geo_province,
    t4.game_interest,
    t4.lat_interest,
    t4.life_last_state,
    t4.new_flag,
    t4.terminal,
    t4.os,
    t4.user_ad_flag,
    t4.ad_type_flag,
    t4.hispace_type_flag,
    t4.push_last_week_login_days,
    t4.push_version,
    t4.app_channel,
    t4.rom_version,
    t4.hispace_version,
    t4.age,
    t4.app_list,
    t4.screen_resolution,
    t4.defined_interest
FROM
(
select
    udf_quota(358313040231942) rowkey,

    udf_quota(
     "X"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t4

union All
SELECT
    t5.rowkey,
    t5.gender,
    t5.sim_mobile_oper,
    t5.hispace_login_days,
    t5.hispace_download_times,
    t5.push_login_days,
    t5.geo_province,
    t5.game_interest,
    t5.lat_interest,
    t5.life_last_state,
    t5.new_flag,
    t5.terminal,
    t5.os,
    t5.user_ad_flag,
    t5.ad_type_flag,
    t5.hispace_type_flag,
    t5.push_last_week_login_days,
    t5.push_version,
    t5.app_channel,
    t5.rom_version,
    t5.hispace_version,
    t5.age,
    t5.app_list,
    t5.screen_resolution,
    t5.defined_interest
FROM
(
select
    udf_quota(358313040231943) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t5

union All
SELECT
    t6.rowkey,
    t6.gender,
    t6.sim_mobile_oper,
    t6.hispace_login_days,
    t6.hispace_download_times,
    t6.push_login_days,
    t6.geo_province,
    t6.game_interest,
    t6.lat_interest,
    t6.life_last_state,
    t6.new_flag,
    t6.terminal,
    t6.os,
    t6.user_ad_flag,
    t6.ad_type_flag,
    t6.hispace_type_flag,
    t6.push_last_week_login_days,
    t6.push_version,
    t6.app_channel,
    t6.rom_version,
    t6.hispace_version,
    t6.age,
    t6.app_list,
    t6.screen_resolution,
    t6.defined_interest
FROM
(
select
    udf_quota(358313040231944) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t6

union All
SELECT
    t7.rowkey,
    t7.gender,
    t7.sim_mobile_oper,
    t7.hispace_login_days,
    t7.hispace_download_times,
    t7.push_login_days,
    t7.geo_province,
    t7.game_interest,
    t7.lat_interest,
    t7.life_last_state,
    t7.new_flag,
    t7.terminal,
    t7.os,
    t7.user_ad_flag,
    t7.ad_type_flag,
    t7.hispace_type_flag,
    t7.push_last_week_login_days,
    t7.push_version,
    t7.app_channel,
    t7.rom_version,
    t7.hispace_version,
    t7.age,
    t7.app_list,
    t7.screen_resolution,
    t7.defined_interest
FROM
(
select
    udf_quota(358313040231945) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "1"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t7

union All
SELECT
    t8.rowkey,
    t8.gender,
    t8.sim_mobile_oper,
    t8.hispace_login_days,
    t8.hispace_download_times,
    t8.push_login_days,
    t8.geo_province,
    t8.game_interest,
    t8.lat_interest,
    t8.life_last_state,
    t8.new_flag,
    t8.terminal,
    t8.os,
    t8.user_ad_flag,
    t8.ad_type_flag,
    t8.hispace_type_flag,
    t8.push_last_week_login_days,
    t8.push_version,
    t8.app_channel,
    t8.rom_version,
    t8.hispace_version,
    t8.age,
    t8.app_list,
    t8.screen_resolution,
    t8.defined_interest
FROM
(
select
    udf_quota(866415010534223) rowkey,

    udf_quota(
     "F"
    ) gender,

    udf_quota(
         "China Mobile"
    ) sim_mobile_oper,

    udf_quota(
    "1_5"
    ) hispace_login_days,

    udf_quota(
    "1_5"
    ) hispace_download_times,

    udf_quota(
    "1_5"
    ) push_login_days,

    udf_quota(
    "500040132000000"
    ) geo_province,

    udf_quota(
    concat('角色游戏','\003','动作冒险')
    ) game_interest,

    udf_quota(
    concat('影音娱乐','\003','图书阅读')
    ) lat_interest,

    udf_quota(
    "silent"
    ) life_last_state,

    udf_quota(
    "new"
    ) new_flag,

    udf_quota(
    "8022"
    ) terminal,

    udf_quota(
    "1.5"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '1'
    ) push_last_week_login_days,

    udf_quota(
    "2503"
    ) push_version,

    udf_quota(
    concat("com.huawei.android.pushagent",'\003',"com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "2.3.018.P2.120515.8022"
    ) rom_version,

    udf_quota(
    "3.8.32"
    ) hispace_version,
    
    udf_quota('0_10') age,
    
    udf_quota(concat('com.tencent.mm','\003','com.tencent.mobileqq')) app_list,

    udf_quota('240*320') screen_resolution,

    udf_quota(concat('白领','\003','学生')) defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t8

union All
SELECT
    t9.rowkey,
    t9.gender,
    t9.sim_mobile_oper,
    t9.hispace_login_days,
    t9.hispace_download_times,
    t9.push_login_days,
    t9.geo_province,
    t9.game_interest,
    t9.lat_interest,
    t9.life_last_state,
    t9.new_flag,
    t9.terminal,
    t9.os,
    t9.user_ad_flag,
    t9.ad_type_flag,
    t9.hispace_type_flag,
    t9.push_last_week_login_days,
    t9.push_version,
    t9.app_channel,
    t9.rom_version,
    t9.hispace_version,
    t9.age,
    t9.app_list,
    t9.screen_resolution,
    t9.defined_interest
FROM
(
select
    udf_quota(357667040247780) rowkey,

    udf_quota(
     "F"
    ) gender,

    udf_quota(
         "China Mobile"
    ) sim_mobile_oper,

    udf_quota(
    "1_5"
    ) hispace_login_days,

    udf_quota(
    "1_5"
    ) hispace_download_times,

    udf_quota(
    "1_5"
    ) push_login_days,

    udf_quota(
    "500040132000000"
    ) geo_province,

    udf_quota(
    '角色游戏'
    ) game_interest,

    udf_quota(
    '影音娱乐'
    ) lat_interest,

    udf_quota(
    "silent"
    ) life_last_state,

    udf_quota(
    "new"
    ) new_flag,

    udf_quota(
    "8022"
    ) terminal,

    udf_quota(
    "1.5"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '1'
    ) push_last_week_login_days,

    udf_quota(
    "2503"
    ) push_version,

    udf_quota(
    concat("com.huawei.android.pushagent",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "2.3.018.P2.120515.8022"
    ) rom_version,

    udf_quota(
    "3.8.32"
    ) hispace_version,
    
    udf_quota('0_10') age,
    
    udf_quota('com.tencent.mm') app_list,

    udf_quota('240*320') screen_resolution,

    udf_quota('白领') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t9

union All
SELECT
    t10.rowkey,
    t10.gender,
    t10.sim_mobile_oper,
    t10.hispace_login_days,
    t10.hispace_download_times,
    t10.push_login_days,
    t10.geo_province,
    t10.game_interest,
    t10.lat_interest,
    t10.life_last_state,
    t10.new_flag,
    t10.terminal,
    t10.os,
    t10.user_ad_flag,
    t10.ad_type_flag,
    t10.hispace_type_flag,
    t10.push_last_week_login_days,
    t10.push_version,
    t10.app_channel,
    t10.rom_version,
    t10.hispace_version,
    t10.age,
    t10.app_list,
    t10.screen_resolution,
    t10.defined_interest
FROM
(
select
    udf_quota(860839019963032) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t10

union All
SELECT
    t11.rowkey,
    t11.gender,
    t11.sim_mobile_oper,
    t11.hispace_login_days,
    t11.hispace_download_times,
    t11.push_login_days,
    t11.geo_province,
    t11.game_interest,
    t11.lat_interest,
    t11.life_last_state,
    t11.new_flag,
    t11.terminal,
    t11.os,
    t11.user_ad_flag,
    t11.ad_type_flag,
    t11.hispace_type_flag,
    t11.push_last_week_login_days,
    t11.push_version,
    t11.app_channel,
    t11.rom_version,
    t11.hispace_version,
    t11.age,
    t11.app_list,
    t11.screen_resolution,
    t11.defined_interest
FROM
(
select
    udf_quota(358313040231946) rowkey,

    udf_quota(
     "X"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t11

union All
SELECT
    t12.rowkey,
    t12.gender,
    t12.sim_mobile_oper,
    t12.hispace_login_days,
    t12.hispace_download_times,
    t12.push_login_days,
    t12.geo_province,
    t12.game_interest,
    t12.lat_interest,
    t12.life_last_state,
    t12.new_flag,
    t12.terminal,
    t12.os,
    t12.user_ad_flag,
    t12.ad_type_flag,
    t12.hispace_type_flag,
    t12.push_last_week_login_days,
    t12.push_version,
    t12.app_channel,
    t12.rom_version,
    t12.hispace_version,
    t12.age,
    t12.app_list,
    t12.screen_resolution,
    t12.defined_interest
FROM
(
select
    udf_quota(358313040231947) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t12


union All
SELECT
    t13.rowkey,
    t13.gender,
    t13.sim_mobile_oper,
    t13.hispace_login_days,
    t13.hispace_download_times,
    t13.push_login_days,
    t13.geo_province,
    t13.game_interest,
    t13.lat_interest,
    t13.life_last_state,
    t13.new_flag,
    t13.terminal,
    t13.os,
    t13.user_ad_flag,
    t13.ad_type_flag,
    t13.hispace_type_flag,
    t13.push_last_week_login_days,
    t13.push_version,
    t13.app_channel,
    t13.rom_version,
    t13.hispace_version,
    t13.age,
    t13.app_list,
    t13.screen_resolution,
    t13.defined_interest
FROM
(
select
    udf_quota(358313040231948) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t13

union All
SELECT
    t14.rowkey,
    t14.gender,
    t14.sim_mobile_oper,
    t14.hispace_login_days,
    t14.hispace_download_times,
    t14.push_login_days,
    t14.geo_province,
    t14.game_interest,
    t14.lat_interest,
    t14.life_last_state,
    t14.new_flag,
    t14.terminal,
    t14.os,
    t14.user_ad_flag,
    t14.ad_type_flag,
    t14.hispace_type_flag,
    t14.push_last_week_login_days,
    t14.push_version,
    t14.app_channel,
    t14.rom_version,
    t14.hispace_version,
    t14.age,
    t14.app_list,
    t14.screen_resolution,
    t14.defined_interest
FROM
(
select
    udf_quota(358313040231949) rowkey,

    udf_quota(
     "M"
    ) gender,

    udf_quota(
         "China Unicom"
    ) sim_mobile_oper,

    udf_quota(
    "6_10"
    ) hispace_login_days,

    udf_quota(
    "30+"
    ) hispace_download_times,

    udf_quota(
    "15+"
    ) push_login_days,

    udf_quota(
    "500010000000000"
    ) geo_province,

    udf_quota(
    '动作冒险'
    ) game_interest,

    udf_quota(
    '社交通讯'
    ) lat_interest,

    udf_quota(
    "grow"
    ) life_last_state,

    udf_quota(
    "old"
    ) new_flag,

    udf_quota(
    "zte_t u880"
    ) terminal,

    udf_quota(
    "2.1"
    ) os,

    udf_quota(
    "0"
    ) user_ad_flag,

    udf_quota(
    "1"
    ) ad_type_flag,

    udf_quota(
    '1'
    ) hispace_type_flag,

    udf_quota(
    '7'
    ) push_last_week_login_days,

    udf_quota(
    "2317"
    ) push_version,

    udf_quota(
    concat("com.huawei.pushtest",'\003',"com.huawei.appmarket")
    ) app_channel,

    udf_quota(
    "U880V1.0.0B05"
    ) rom_version,

    udf_quota(
    "4.1.0"
    ) hispace_version,
    
    udf_quota('11_20') age,
    
    udf_quota('com.tencent.mobileqq') app_list,

    udf_quota('240*432') screen_resolution,

    udf_quota('学生') defined_interest

from tmp_alliance1dot5_all_info_ds limit 1
) t14

) n order by n.rowkey;


