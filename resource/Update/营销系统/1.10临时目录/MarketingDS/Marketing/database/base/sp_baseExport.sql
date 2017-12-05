DELIMITER // 
DROP PROCEDURE IF EXISTS sp_baseExport;
CREATE PROCEDURE sp_baseExport (
     out pRetCode               int,
     in pActivtiy_id            int,
     in pMedia_type             int,
     in pArea_type              int,
     in pUser_type              int,
     in pPt_dStart              varchar(12),
     in pPt_dEnd                varchar(12)
     
)
  proc: BEGIN
  	
  declare tmpDateStart        varchar(10) default null;
  declare tmpDateEnd          varchar(10) default null;
  declare temprovince         varchar(64) default null;
  
  set tmpDateStart = date_format(pPt_dStart,'%Y%m%d');
  set tmpDateEnd   = date_format(pPt_dEnd,'%Y%m%d');  
  set pRetCode = 0;
  if ( pArea_type is null ) then
      set pArea_type = 0;
  end if;
  if (pArea_type != 0) then
     select dic_value into temprovince  from t_mkt_common_dic_info where type='province' and dic_key = pArea_type; 
  end if;

  if (pUser_type is null) then
      set pUser_type = 0;
  end if;



-- 表格展示

select  
    fk.media_name   as mediaName,
    fk.bg_pv_sum    as bgPv, 
    fk.bg_uv_sum    as bgUv, 
    fk.dj_pv_sum    as djPv, 
    fk.dj_uv_sum    as djUv,
    fk.orderId  

from
(
   select c.media_id   as media_id,
          c.media_name as media_name,
          if(c.bg_pv_sum is null,0,c.bg_pv_sum)  as bg_pv_sum,
          if(c.bg_uv_sum is null,0,c.bg_uv_sum)  as bg_uv_sum,
          if(c.dj_pv_sum is null,0,c.dj_pv_sum)  as dj_pv_sum,
          if(c.dj_uv_sum is null,0,c.dj_uv_sum)  as dj_uv_sum,
          c.orderId                              as orderId
   from 
   (
       select 
           f.media_id   as media_id,
           f.media_name as media_name,
           f.bg_pv_sum  as bg_pv_sum,
           f.bg_uv_sum  as bg_uv_sum,
           f.dj_pv_sum  as dj_pv_sum,
           f.dj_uv_sum  as dj_uv_sum,
           1            as orderId 
       from 
       (
           select
           t.media_id         as media_id,
           min(t.media_name)  as media_name,
           sum(t0.bg_pv)      as bg_pv_sum,
           sum(t0.bg_uv)      as bg_uv_sum,
           sum(t0.dj_pv)      as dj_pv_sum,
           sum(t0.dj_uv)      as dj_uv_sum
           from 
           (
           select sid,media_name,media_id from dim_mkt_base_info_ds
           where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
           if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
           ) t
           join
           (
           select 
                  bg_pv,
                  bg_uv,
                  dj_pv,
                  dj_uv,
                  sid
                  from dw_mkt_result_dm
           where user_type = ifnull(pUser_type,user_type) and pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
           province = ifnull(temprovince,province) and hour='NA' and city='NA'
           ) t0
           on t.sid = t0.sid
           group by t.media_id
       ) f
       order by f.bg_uv_sum DESC,f.dj_uv_sum DESC
       ) c
       
       union all
       -- -------------------------------------------
       
       select 
           kk.media_id   as media_id,
           kk.media_name as media_name,
           kk.bg_pv_sum  as bg_pv_sum,
           kk.bg_uv_sum  as bg_uv_sum,
           kk.dj_pv_sum  as dj_pv_sum,
           kk.dj_uv_sum  as dj_uv_sum,
           kk.orderId    as orderId
           
       from 
       (
       select
           if(sum(k.bg_pv_sum) is null,null,-1)                  as media_id,
           if(sum(k.bg_pv_sum) is null,null,'Total')             as media_name,
           sum(k.bg_pv_sum)   as bg_pv_sum,
           sum(k.bg_uv_sum)   as bg_uv_sum,
           sum(k.dj_pv_sum)   as dj_pv_sum,
           sum(k.dj_uv_sum)   as dj_uv_sum,
           if(sum(k.bg_pv_sum) is null,null,0)                    as orderId
       from
       (
           select media_id,media_name,bg_pv_sum,bg_uv_sum,dj_pv_sum,dj_uv_sum from 
           (
               select
               t.media_id         as media_id,
               min(t.media_name)  as media_name,
               sum(t0.bg_pv)      as bg_pv_sum,
               sum(t0.bg_uv)      as bg_uv_sum,
               sum(t0.dj_pv)      as dj_pv_sum,
               sum(t0.dj_uv)      as dj_uv_sum
               from 
               (
               select sid,media_name,media_id from dim_mkt_base_info_ds
               where activtiy_id = ifnull(pActivtiy_id,activtiy_id) and 
               if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
               ) t
               join
               (
                    select 
                           bg_pv,
                           bg_uv,
                           dj_pv,
                           dj_uv,
                           sid
                           from dw_mkt_result_dm
                    where 
                    user_type = ifnull(pUser_type,user_type) and 
                    pt_d<=ifnull(tmpDateEnd,pt_d) and pt_d>=ifnull(tmpDateStart,pt_d) and 
                    province = ifnull(temprovince,province) and 
                    hour='NA' and city='NA'
               ) t0
               on t.sid = t0.sid
               group by t.media_id
           ) f
       ) k
       limit 1
       ) kk
       
       -- -------------------------------------------
) fk
where
fk.media_id is not null
order by fk.orderId ,fk.bg_uv_sum DESC,fk.dj_uv_sum DESC
;

   end
   
   //
DELIMITER ;