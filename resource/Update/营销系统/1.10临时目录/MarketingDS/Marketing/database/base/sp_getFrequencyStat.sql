
DROP PROCEDURE IF EXISTS `sp_getFrequencyStat`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_getFrequencyStat`(

     out pRetCode               int,
     in pOperator               varchar(50),
     in pActivtiy_id            int,
     in pMedia_type             int,
     in pUser_type              int,
     in pPt_d                   varchar(12),
     in pArea_type              int,
     in pProvince_id            int,
     in ptype                    int, -- 0 曝光 1 点击 2 着陆
     in salectMax               int  -- 展示个数

)
proc: BEGIN

  declare tmpDateEnd        varchar(10) default null;
  declare tmpDateStart      varchar(10) default null;
  declare tmpCount          int default -1;
  declare temprovince            varchar(64) default null;
  declare tmpSum          int default 0;
  
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



select q.media_id as media_id,
       q.media_name as media_name,
       q.province_name as province_name,
       q.province_id as province_id,
       q.frequency as frequency,
       q.exp_frequency_users as exp_frequency_users,
       q.click_frequency_users as click_frequency_users,
       q.landing_frequency_users as landing_frequency_users
    from 


(
select z.media_id as media_id,
       z.media_name as media_name,
       z.province_name as province_name,
       z.province_id as province_id,
       z.frequency as frequency,
       z.exp_frequency_users as exp_frequency_users,
       z.click_frequency_users as click_frequency_users,
       z.landing_frequency_users as landing_frequency_users
    from 

(
select abc1.media_id as media_id,
       abc1.media_name as media_name,
       abc1.province_name as province_name,
       abc1.province_id as province_id,
       abc1.frequency as frequency,
       abc1.exp_frequency_users as exp_frequency_users,
       abc1.click_frequency_users as click_frequency_users,
       abc1.landing_frequency_users as landing_frequency_users
    from 

-- 所有媒体，所有省份，所有点击、曝光、到达频次

(
select 
       t.media_id as media_id,
       t.media_name as media_name,
       t1.province as province_name,
       t2.province_id as province_id,
       t1.frequency as frequency,
       t1.exp_frequency_users as exp_frequency_users,
       t1.click_frequency_users as click_frequency_users,
       t1.landing_frequency_users as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t1
on t.activtiy_id = t1.activtiy_id and t.media_id = t1.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t2
on t1.province = t2.province_name


union all

select 
       
       '0' as media_id,
       'total' as media_name,
       max(t01.province) as province_name,
       t02.province_id as province_id,
       t01.frequency as frequency, sum(t01.exp_frequency_users) as exp_frequency_users,
       sum(t01.click_frequency_users) as click_frequency_users,
       sum(t01.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t0
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t01
on t0.activtiy_id = t01.activtiy_id and t0.media_id = t01.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t02
on t01.province = t02.province_name
group by t02.province_id , t01.frequency 

)abc1

union all

select abc2.media_id as media_id,
       abc2.media_name as media_name,
       abc2.province_name as province_name,
       abc2.province_id as province_id,
       abc2.frequency as frequency,
       abc2.exp_frequency_users as exp_frequency_users,
       abc2.click_frequency_users as click_frequency_users,
       abc2.landing_frequency_users as landing_frequency_users
    from 

-- 所有媒体，所有省份，所有点击、曝光、到达频次

(
select 
       t.media_id as media_id,
       t1.frequency as frequency,
       '0' as province_id,
       min(t.media_name) as media_name,
       '中国大陆' as province_name,
       sum(t1.exp_frequency_users) as exp_frequency_users,
       sum(t1.click_frequency_users) as click_frequency_users,
       sum(t1.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t1
on t.activtiy_id = t1.activtiy_id and t.media_id = t1.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t2
on t1.province = t2.province_name
group by t.media_id ,t1.frequency

union all


select 
       '0' as media_id,
       t01.frequency as frequency,
       '0' as province_id,
       'total' as media_name,
       '中国大陆' as province_name,
       sum(t01.exp_frequency_users) as exp_frequency_users,
       sum(t01.click_frequency_users) as click_frequency_users,
       sum(t01.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t0
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t01
on t0.activtiy_id = t01.activtiy_id and t0.media_id = t01.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t02
on t01.province = t02.province_name
group by t01.frequency

)abc2
)z

union all

select z1.media_id as media_id,
       min(z1.media_name) as media_name,
       min(z1.province_name) as province_name,
       z1.province_id as province_id,
       '3+' as frequency,
       sum(z1.exp_frequency_users) as exp_frequency_users,
       sum(z1.click_frequency_users) as click_frequency_users,
       sum(z1.landing_frequency_users) as landing_frequency_users
    from 

(
select abc1.media_id as media_id,
       abc1.media_name as media_name,
       abc1.province_name as province_name,
       abc1.province_id as province_id,
       abc1.frequency as frequency,
       abc1.exp_frequency_users as exp_frequency_users,
       abc1.click_frequency_users as click_frequency_users,
       abc1.landing_frequency_users as landing_frequency_users
    from 

-- 所有媒体，所有省份，所有点击、曝光、到达频次

(
select 
       t.media_id as media_id,
       t.media_name as media_name,
       t1.province as province_name,
       t2.province_id as province_id,
       t1.frequency as frequency,
       t1.exp_frequency_users as exp_frequency_users,
       t1.click_frequency_users as click_frequency_users,
       t1.landing_frequency_users as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t1
on t.activtiy_id = t1.activtiy_id and t.media_id = t1.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t2
on t1.province = t2.province_name

union all

select 
       
       '0' as media_id,
       'total' as media_name,
       max(t01.province) as province_name,
       t02.province_id as province_id,
       t01.frequency as frequency, sum(t01.exp_frequency_users) as exp_frequency_users,
       sum(t01.click_frequency_users) as click_frequency_users,
       sum(t01.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t0
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t01
on t0.activtiy_id = t01.activtiy_id and t0.media_id = t01.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t02
on t01.province = t02.province_name
group by t02.province_id , t01.frequency 


)abc1

union all

select abc2.media_id as media_id,
       abc2.media_name as media_name,
       abc2.province_name as province_name,
       abc2.province_id as province_id,
       abc2.frequency as frequency,
       abc2.exp_frequency_users as exp_frequency_users,
       abc2.click_frequency_users as click_frequency_users,
       abc2.landing_frequency_users as landing_frequency_users
    from 

-- 所有媒体，所有省份，所有点击、曝光、到达频次

(
select 
       t.media_id as media_id,
       t1.frequency as frequency,
       '0' as province_id,
       min(t.media_name) as media_name,
       '中国大陆' as province_name,
       sum(t1.exp_frequency_users) as exp_frequency_users,
       sum(t1.click_frequency_users) as click_frequency_users,
       sum(t1.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t1
on t.activtiy_id = t1.activtiy_id and t.media_id = t1.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t2
on t1.province = t2.province_name
group by t.media_id ,t1.frequency

union all

select 
       '0' as media_id,
       t01.frequency as frequency,
       '0' as province_id,
       'total' as media_name,
       '中国大陆' as province_name,
       sum(t01.exp_frequency_users) as exp_frequency_users,
       sum(t01.click_frequency_users) as click_frequency_users,
       sum(t01.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
group by activtiy_id, media_name,media_id 
)t0
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t01
on t0.activtiy_id = t01.activtiy_id and t0.media_id = t01.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t02
on t01.province = t02.province_name
group by t01.frequency

)abc2
)z1
where z1.frequency>=3
group by z1.media_id,z1.province_id

union all

select z2.media_id as media_id,
       min(z2.media_name) as media_name,
       min(z2.province_name) as province_name,
       z2.province_id as province_id,
       '15+' as frequency,
       sum(z2.exp_frequency_users) as exp_frequency_users,
       sum(z2.click_frequency_users) as click_frequency_users,
       sum(z2.landing_frequency_users) as landing_frequency_users
    from 

(
select abc1.media_id as media_id,
       abc1.media_name as media_name,
       abc1.province_name as province_name,
       abc1.province_id as province_id,
       abc1.frequency as frequency,
       abc1.exp_frequency_users as exp_frequency_users,
       abc1.click_frequency_users as click_frequency_users,
       abc1.landing_frequency_users as landing_frequency_users
    from 

-- 所有媒体，所有省份，所有点击、曝光、到达频次

(
select 
       t.media_id as media_id,
       t.media_name as media_name,
       t1.province as province_name,
       t2.province_id as province_id,
       t1.frequency as frequency,
       t1.exp_frequency_users as exp_frequency_users,
       t1.click_frequency_users as click_frequency_users,
       t1.landing_frequency_users as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
group by activtiy_id, media_name,media_id 
)t
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t1
on t.activtiy_id = t1.activtiy_id and t.media_id = t1.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t2
on t1.province = t2.province_name


union all

select 
       
       '0' as media_id,
       'total' as media_name,
       max(t01.province) as province_name,
       t02.province_id as province_id,
       t01.frequency as frequency, sum(t01.exp_frequency_users) as exp_frequency_users,
       sum(t01.click_frequency_users) as click_frequency_users,
       sum(t01.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t0
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t01
on t0.activtiy_id = t01.activtiy_id and t0.media_id = t01.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t02
on t01.province = t02.province_name
group by t02.province_id , t01.frequency 

)abc1

union all

select abc2.media_id as media_id,
       abc2.media_name as media_name,
       abc2.province_name as province_name,
       abc2.province_id as province_id,
       abc2.frequency as frequency,
       abc2.exp_frequency_users as exp_frequency_users,
       abc2.click_frequency_users as click_frequency_users,
       abc2.landing_frequency_users as landing_frequency_users
    from 

-- 所有媒体，所有省份，所有点击、曝光、到达频次

(
select 
       t.media_id as media_id,
       t1.frequency as frequency,
       '0' as province_id,
       min(t.media_name) as media_name,
       '中国大陆' as province_name,
       sum(t1.exp_frequency_users) as exp_frequency_users,
       sum(t1.click_frequency_users) as click_frequency_users,
       sum(t1.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t1
on t.activtiy_id = t1.activtiy_id and t.media_id = t1.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t2
on t1.province = t2.province_name
group by t.media_id ,t1.frequency

union all

select 
       '0' as media_id,
       t01.frequency as frequency,
       '0' as province_id,
       'total' as media_name,
       '中国大陆' as province_name,
       sum(t01.exp_frequency_users) as exp_frequency_users,
       sum(t01.click_frequency_users) as click_frequency_users,
       sum(t01.landing_frequency_users) as landing_frequency_users
from
(
select activtiy_id,
       media_name,
       media_id
              
from dim_mkt_base_info_ds
where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2))) 
group by activtiy_id, media_name,media_id
)t0
left outer join
(
select 
  activtiy_id,
  media_id,
  province,
  frequency,
  exp_frequency_users,
  click_frequency_users,
  landing_frequency_users
  from
  dw_exposure_frequency_dm
  where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and area_type = ifnull(pArea_type,area_type)

)t01
on t0.activtiy_id = t01.activtiy_id and t0.media_id = t01.media_id
left outer join
(
select
   dic_key as province_id,
   dic_value as province_name
   from
   t_mkt_common_dic_info
   where
   type = 'province'
) t02
on t01.province = t02.province_name
group by t01.frequency

)abc2
)z2
where z2.frequency>=15
group by z2.media_id,z2.province_id
)q


where  q.province_id=ifnull(pProvince_id,province_id) and q.frequency is not null
order by q.media_id ASC;

     
     
-- ad by sxy 20160116 for 表格展示

  
  if (pProvince_id != 0) then
     select dic_value into temprovince  from t_mkt_common_dic_info where type='province' and dic_key = pProvince_id; 
  end if;
  
   select              
     if(ptype=0,sum(exp_frequency_users),if(ptype=1,sum(click_frequency_users),sum(landing_frequency_users))) into tmpSum
  from dw_exposure_frequency_dm
  where 
     activtiy_id=ifnull(pActivtiy_id,activtiy_id)
     and province=ifnull(temprovince,province)
     and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d)
     and user_type = ifnull(pUser_type,user_type)
     and area_type = ifnull(pArea_type,area_type)
     and CAST(frequency as SIGNED) <=salectMax
  group by activtiy_id;
  
  if(tmpSum<=0) then
      set pActivtiy_id = -125125;
  end if;
-- 0 曝光 1 点击 2 着陆
select
     t.mediaName   as mediaName,
     t.frequency   as frequency,
     t.fk          as fk,
     t.orderBy
from
(
    select 
        daye.media_name as mediaName,
        yun.frequency   as frequency,
        yun.fk          as fk,
        1               as orderBy
    from
    (
        select 
           media_name,
           media_id              
        from dim_mkt_base_info_ds
        where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
        group by media_id,media_name
    ) daye
    left outer join
    (
        select cao.media_id as media_id,cao.frequency as frequency,cao.fk as fk
        from
        (
           select media_id,
               frequency,
               if(ptype=0,sum(exp_frequency_users),if(ptype=1,sum(click_frequency_users),sum(landing_frequency_users))) as fk
           from dw_exposure_frequency_dm
           where 
               activtiy_id=ifnull(pActivtiy_id,activtiy_id)
               and province=ifnull(temprovince,province)
               and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d)
               and user_type = ifnull(pUser_type,user_type)
               and area_type = ifnull(pArea_type,area_type)
               and CAST(frequency as SIGNED) <=salectMax
           group by media_id,frequency
        ) cao
    ) yun
    on daye.media_id = yun.media_id
    where yun.frequency is not null
    
    union all
    
    
    select 
        'Total'         as mediaName,
        yun.frequency   as frequency,
        sum(yun.fk)     as fk,
        0               as orderBy
    from
    (
        select 
           media_name,
           media_id              
        from dim_mkt_base_info_ds
        where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
        group by media_id,media_name
    ) daye
    left outer join
    (
        select cao.media_id as media_id,cao.frequency as frequency,cao.fk as fk
        from
        (
           select media_id,
               frequency,
               if(ptype=0,sum(exp_frequency_users),if(ptype=1,sum(click_frequency_users),sum(landing_frequency_users))) as fk
           from dw_exposure_frequency_dm
           where 
               activtiy_id=ifnull(pActivtiy_id,activtiy_id)
               and province=ifnull(temprovince,province)
               and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d)
               and user_type = ifnull(pUser_type,user_type)
               and area_type = ifnull(pArea_type,area_type)
               and CAST(frequency as SIGNED) <=salectMax
           group by media_id,frequency
        ) cao
    ) yun
    on daye.media_id = yun.media_id
    where yun.frequency is not null
    group by yun.frequency
    
    
) t
order by t.orderBy,t.mediaName,t.frequency;   


   end//
DELIMITER ;