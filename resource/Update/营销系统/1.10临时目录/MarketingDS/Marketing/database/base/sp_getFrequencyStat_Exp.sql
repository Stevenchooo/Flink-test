
DROP PROCEDURE IF EXISTS `sp_getFrequencyStat_Exp`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_getFrequencyStat_Exp`(

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
order by t.orderBy,t.mediaName,CAST(t.frequency as SIGNED);   


   end//
DELIMITER ;