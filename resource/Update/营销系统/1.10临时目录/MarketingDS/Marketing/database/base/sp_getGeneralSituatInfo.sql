DROP PROCEDURE IF EXISTS `sp_getGeneralSituatInfo`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_getGeneralSituatInfo`(
     out pRetCode               int,
     in pOperator               varchar(50),
     in pActivtiy_id            int,
     in pMedia_type             int,
     in pUser_type              int,
     in pPt_d                   varchar(12),
     in pArea_type              int
)
proc: BEGIN
  	
  declare tmpDateEnd        varchar(10) default null;
  declare tmpDateStart      varchar(10) default null;
  declare tmpCount          int default -1;
  
  -- set tmpDateEnd = date_format(pPt_d,'%Y%m%d');
  set pRetCode = 0;
  select count(*) into tmpCount from dim_mkt_base_info_ds where activtiy_id = pActivtiy_id;
  if ( tmpCount < 0 ) then
    set pRetCode = 125125;
    leave proc;
  end if;
  
  select begin_date into tmpDateStart from dim_mkt_base_info_ds where activtiy_id = pActivtiy_id limit 1;
  select end_date into tmpDateEnd from dim_mkt_base_info_ds where activtiy_id = pActivtiy_id  limit 1;
  
  if ( tmpDateStart is null) then
    set tmpDateStart = date_format(pPt_d,'%Y%m%d');
  end if;
  
  if ( tmpDateEnd is null) then
    set tmpDateEnd = date_format(pPt_d,'%Y%m%d');
  end if;
  
  
  if ( tmpDateStart > pPt_d ) then
    set tmpDateStart = pPt_d;  
  end if;
  
  set tmpDateEnd = date_format(pPt_d,'%Y%m%d');
  

-- 总体数据概况
select 
    if(sum(bg_pv) is null,0,sum(bg_pv))      as all_bg_pv,
    if(max(fk.estimate_exp) = 0 or max(fk.estimate_exp) is null,'-',if(sum(bg_pv)=0,'-',CONCAT(format(100*sum(bg_pv)/max(fk.estimate_exp),2),'%'))) as bg_pv_rate,
    if(sum(dj_pv) is null ,0,sum(dj_pv))      as all_dj_pv,
    if(max(fk.estimate_click) = 0 or max(fk.estimate_click) is null,'-',if(sum(dj_pv)=0,'-',CONCAT(format(100*sum(dj_pv)/max(fk.estimate_click),2),'%'))) as dj_pv_rate,
    CONCAT(if(if(sum(bg_pv)=0,0,format(100*sum(dj_pv)/sum(bg_pv),2)) is null,0,if(sum(bg_pv)=0,0,format(100*sum(dj_pv)/sum(bg_pv),2))),'%')    as ctr,
    if(sum(bg_uv) is null,0,sum(bg_uv))      as all_bg_uv
from 
(
    select 
         t.estimate_exp    as estimate_exp,
         t.estimate_click  as estimate_click,
         t.activtiy_id     as activtiy_id,
         t.activity_name   as activity_name,
         t0.bg_pv          as bg_pv,
         t0.dj_pv          as dj_pv,
         t0.bg_uv          as bg_uv
    from 
    (
    select 
           estimate_exp,
           estimate_click,
           sid,
           activtiy_id,
           activity_name
    from dim_mkt_base_info_ds
    where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
    if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
    ) t
    left outer join
    (
    select 
           bg_pv,
           dj_pv,
           bg_uv,
           sid
    from dw_mkt_result_dm
    where 
           user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
           area_type = ifnull(pArea_type,area_type) and hour='NA' and city='NA'
    ) t0
    on t.sid = t0.sid
)  fk
group by fk.activtiy_id
;


-- 媒体排名BG
select  
    fk.media_id     as mt_media_id,  
    fk.media_name   as mt_media_name,
    if(fk.bg_pv_sum is null,0,fk.bg_pv_sum)    as mt_bg_pv_sum, 
    if(fk.bg_uv_sum is null,0,fk.bg_uv_sum)    as mt_bg_uv_sum 

from
(
   select c.media_id   as media_id,
          c.media_name as media_name,
          c.bg_pv_sum  as bg_pv_sum,
          c.bg_uv_sum  as bg_uv_sum
   from 
   (
       select 
           f.media_id   as media_id,
           f.media_name as media_name,
           f.bg_pv_sum  as bg_pv_sum,
           f.bg_uv_sum  as bg_uv_sum
       from 
       (
           select
           t.media_id         as media_id,
           min(t.media_name)  as media_name,
           sum(t0.bg_pv)      as bg_pv_sum,
           sum(t0.bg_uv)      as bg_uv_sum
           from 
           (
           select sid,media_name,media_id from dim_mkt_base_info_ds
           where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
           if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
           ) t
           left outer join
           (
           select 
                  bg_pv,
                  bg_uv,
                  sid
                  from dw_mkt_result_dm
           where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
           area_type = ifnull(pArea_type,area_type) and hour='NA' and city='NA'
           ) t0
           on t.sid = t0.sid
           group by t.media_id
       ) f
       order by f.bg_uv_sum DESC
       limit 15
       ) c
       
       union all
       
       select 
           kk.media_id   as media_id,
           kk.media_name as media_name,
           kk.bg_pv_sum  as bg_pv_sum,
           kk.bg_uv_sum  as bg_uv_sum
       from 
       (
       select
           if(sum(k.bg_pv_sum) is null,null,-1)                 as media_id,
           if(sum(k.bg_pv_sum) is null,null,'其他')             as media_name,
           sum(k.bg_pv_sum)   as bg_pv_sum,
           sum(k.bg_uv_sum)   as bg_uv_sum
       from
       (
           select media_id,media_name,bg_pv_sum,bg_uv_sum from 
           (
               select
               t.media_id         as media_id,
               min(t.media_name)  as media_name,
               sum(t0.bg_pv)      as bg_pv_sum,
               sum(t0.bg_uv)      as bg_uv_sum
               from 
               (
               select sid,media_name,media_id from dim_mkt_base_info_ds
               where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
               if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
               ) t
               left outer join
               (
                    select 
                           bg_pv,
                           bg_uv,
                           sid
                           from dw_mkt_result_dm
                    where 
                    user_type = ifnull(pUser_type,user_type) and 
                    pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
                    area_type = ifnull(pArea_type,area_type) and 
                    hour='NA' and city='NA'
               ) t0
               on t.sid = t0.sid
               group by t.media_id
           ) f
           order by f.bg_uv_sum DESC
           limit 15,2000
       ) k
       limit 1
       ) kk
) fk
where fk.media_id is not null and (fk.bg_pv_sum !=0 or fk.bg_uv_sum !=0 );

-- 媒体排名DJ
select  
    fk.media_id     as mt_media_id,  
    fk.media_name   as mt_media_name,
    if(fk.dj_pv_sum is null,0,fk.dj_pv_sum)    as mt_dj_pv_sum, 
    if(fk.dj_uv_sum is null,0,fk.dj_uv_sum)    as mt_dj_uv_sum 

from
(
   select c.media_id   as media_id,
          c.media_name as media_name,
          c.dj_pv_sum  as dj_pv_sum,
          c.dj_uv_sum  as dj_uv_sum
   from 
   (
       select 
           f.media_id   as media_id,
           f.media_name as media_name,
           f.dj_pv_sum  as dj_pv_sum,
           f.dj_uv_sum  as dj_uv_sum
       from 
       (
           select
           t.media_id         as media_id,
           min(t.media_name)  as media_name,
           sum(t0.dj_pv)      as dj_pv_sum,
           sum(t0.dj_uv)      as dj_uv_sum
           from 
           (
           select sid,media_name,media_id from dim_mkt_base_info_ds
           where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
           if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
           ) t
           left outer join
           (
           select 
                  dj_pv,
                  dj_uv,
                  sid
                  from dw_mkt_result_dm
           where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
           area_type = ifnull(pArea_type,area_type) and hour='NA' and city='NA'
           ) t0
           on t.sid = t0.sid
           group by t.media_id
       ) f
       order by f.dj_uv_sum DESC
       limit 15
       ) c
       
       union all
       
       select 
           kk.media_id   as media_id,
           kk.media_name as media_name,
           kk.dj_pv_sum  as dj_pv_sum,
           kk.dj_uv_sum  as dj_uv_sum
       from 
       (
       select
           if(sum(k.dj_pv_sum) is null,null,-1)                 as media_id,
           if(sum(k.dj_pv_sum) is null,null,'其他')             as media_name,
           sum(k.dj_pv_sum)   as dj_pv_sum,
           sum(k.dj_uv_sum)   as dj_uv_sum
       from
       (
           select media_id,media_name,dj_pv_sum,dj_uv_sum from 
           (
               select
               t.media_id         as media_id,
               min(t.media_name)  as media_name,
               sum(t0.dj_pv)      as dj_pv_sum,
               sum(t0.dj_uv)      as dj_uv_sum
               from 
               (
               select sid,media_name,media_id from dim_mkt_base_info_ds
               where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
               if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
               ) t
               left outer join
               (
                    select 
                           dj_pv,
                           dj_uv,
                           sid
                           from dw_mkt_result_dm
                    where 
                    user_type = ifnull(pUser_type,user_type) and 
                    pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
                    area_type = ifnull(pArea_type,area_type) and 
                    hour='NA' and city='NA'
               ) t0
               on t.sid = t0.sid
               group by t.media_id
           ) f
           order by f.dj_uv_sum DESC
           limit 15,2000
       ) k
       limit 1
       ) kk
) fk
where fk.media_id is not null and (fk.dj_pv_sum !=0 or fk.dj_uv_sum !=0 );


-- 24小时趋势

select
    t0.hour        as hour, 
    sum(t0.bg_pv)  as hour_bg_pv_sum,
    sum(t0.dj_pv)  as hour_dj_pv_sum
from 
(
    select 
        sid 
    from dim_mkt_base_info_ds
    where 
        activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
        if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
) t
join
(
    select 
       bg_pv,
       dj_pv,
       hour,
       sid
    from dw_mkt_result_dm
    where 
        user_type = ifnull(pUser_type,user_type) and 
        -- pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
        pt_d = date_format(pPt_d,'%Y%m%d') and
        area_type = ifnull(pArea_type,area_type) and 
        hour!='NA' and  city='NA'
) t0
on t.sid = t0.sid
group by t0.hour
order by t0.hour;

-- 点击趋势
select
    t.media_id        as dj_media_id,
    t.media_name      as dj_media_name,
    if(t0.click_frequency_users_sum is null ,0,t0.click_frequency_users_sum) as click_frequency_users_sum,
    if(t0.frequency is null,0,t0.frequency)         as frequency
from 
(
    select 
        media_id,
        media_name         
    from dim_mkt_base_info_ds
    where 
        activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
        if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
    group by  media_id,media_name
) t
join
(
    select 
       sum(click_frequency_users) as click_frequency_users_sum,
       media_id                   as media_id,
       frequency                  as frequency
    from dw_exposure_frequency_dm
    where 
        user_type = ifnull(pUser_type,user_type) and 
        pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
        area_type = ifnull(pArea_type,area_type) and 
        activtiy_id=ifnull(pActivtiy_id,activtiy_id) and
        frequency<=20
        
    group by media_id,frequency
        
) t0
on t.media_id = t0.media_id;


-- 地域


select 
    sum(bg_pv)      as area_bg_pv,
    sum(dj_pv)      as area_dj_pv,
    sum(bg_uv)      as area_bg_uv,
    sum(dj_uv)      as area_dj_uvm,
    substring_index(substring_index(province,'省',1),'市',1)        as province
from 
(
    select 
         t0.bg_pv          as bg_pv,
         t0.dj_pv          as dj_pv,
         t0.bg_uv          as bg_uv,
         t0.dj_uv          as dj_uv,
         t0.province       as province
    from 
    (
    select 
           sid,
           activtiy_id
    from dim_mkt_base_info_ds
    where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
    if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
    ) t
    join
    (
    select 
           bg_pv,
           dj_pv,
           bg_uv,
           dj_uv,
           sid,
           province
           from dw_mkt_result_dm
           where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
                 area_type = ifnull(pArea_type,area_type) and hour='NA' and city='NA'
    ) t0
    on t.sid = t0.sid
)  fk
group by fk.province;


-- 转换


    select fk1.allp_bg_pv  as allp_bg_pv,
           fk1.allp_bg_uv  as allp_bg_uv,
           fk2.wdp_bg_pv   as wdp_bg_pv,
           fk2.wdp_bg_uv   as wdp_bg_uv,
           CONCAT(if(fk1.allp_bg_uv = 0,0,format(100*fk2.wdp_bg_uv/fk1.allp_bg_uv,2)),'%') as uv_TranRate,
           CONCAT(if(fk1.allp_bg_pv = 0,0,format(100*fk2.wdp_bg_pv/fk1.allp_bg_pv,2)),'%') as pv_TranRate
    from
    (    
        select 
            if(sum(fk.bg_pv) is null,0,sum(fk.bg_pv))      as allp_bg_pv,
            if(sum(fk.bg_uv) is null,0,sum(fk.bg_uv))      as allp_bg_uv,
            fk.activtiy_id     as activtiy_id
        from 
        (
            select 
                 t.activtiy_id     as activtiy_id,
                 t.activity_name   as activity_name,
                 t0.bg_pv          as bg_pv,
                 t0.bg_uv          as bg_uv
            from 
            (
            select 
                   sid,
                   activtiy_id,
                   activity_name
            from dim_mkt_base_info_ds
            where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
            if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
            ) t
            left outer join
            (
            select 
                   bg_pv,
                   bg_uv,
                   sid
                   from dw_mkt_result_dm
                   where user_type = '0' and pt_d=ifnull(date_format(date_add(pPt_d,interval -1 day),'%Y%m%d'),pt_d) and area_type = ifnull(pArea_type,area_type) and hour='NA' and city='NA'
            ) t0
            on t.sid = t0.sid
        )  fk
        group by fk.activtiy_id
    ) fk1
    left outer join
    (    
        select 
            if(sum(fk.bg_pv) is null,0,sum(fk.bg_pv))      as wdp_bg_pv,
            if(sum(fk.bg_uv) is null,0,sum(fk.bg_uv))      as wdp_bg_uv,
            fk.activtiy_id     as activtiy_id
        from 
        (
            select 
                 t.activtiy_id     as activtiy_id,
                 t.activity_name   as activity_name,
                 t0.bg_pv          as bg_pv,
                 t0.bg_uv          as bg_uv
            from 
            (
            select 
                   sid,
                   activtiy_id,
                   activity_name
            from dim_mkt_base_info_ds
            where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
            if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
            ) t
            left outer join
            (
            select 
                   bg_pv,
                   bg_uv,
                   sid
                   from dw_mkt_result_dm
                   where user_type = '1' and pt_d=ifnull(date_format(pPt_d,'%Y%m%d'),pt_d) and area_type = ifnull(pArea_type,area_type) and hour='NA' and city='NA'
            ) t0
            on t.sid = t0.sid
        )  fk
        group by fk.activtiy_id
    ) fk2
    on fk1.activtiy_id = fk2.activtiy_id ;


   end//
DELIMITER ;