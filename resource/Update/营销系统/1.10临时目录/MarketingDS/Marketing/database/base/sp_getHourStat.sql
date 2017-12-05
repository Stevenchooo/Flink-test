DELIMITER // 
DROP PROCEDURE IF EXISTS sp_getHourStat;
CREATE PROCEDURE sp_getHourStat (
     out pRetCode               int,
     in pOperator               varchar(50),
     in pActivtiy_id            int,
     in pFirst_id               int,
     in pMedia_type             int,
     in pPt_d                   varchar(12)
)
  proc: BEGIN
  	
  declare tmpCount          int default -1;
  declare tmpPt_d          varchar(12);
  
  set tmpPt_d   = date_format(pPt_d,'%Y%m%d');
  set pRetCode = 0;
  select count(*) into tmpCount from dim_mkt_base_info_ds where activtiy_id = pActivtiy_id;
  if ( tmpCount < 0 ) then
    set pRetCode = 125125;
    leave proc;
  end if;
  
-- 小时数据图表展示



select z.first_id as img_first_id,
       z.first_name as img_first_name,
       z.sub_id as img_sub_id,
       z.sub_name as img_sub_name,
       z.hour_time as img_hour_time,
       z.bg_pv as img_bg_pv,
       z.dj_pv as img_dj_pv
from

(
select tt.media_id as first_id,
       tt.media_name as first_name,
       tt.sid as sub_id,
       tt.ad_position as sub_name,
       tt.hour as hour_time,
       tt.bg_pv as bg_pv,
       tt.dj_pv as dj_pv
from(

select min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.dj_pv) as dj_pv
       from
(select media_id,
       media_name,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
       and if(pFirst_id = -1,1 = 1, media_id=pFirst_id)
)t1
left outer join
(select sid,
        hour,
        bg_pv,
        dj_pv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0' and hour!='NA'

)t2
on t1.sid = t2.sid
where t2.hour is not null
group by t1.sid,t2.hour

union all

select t10.media_id as media_id,
       min(t10.media_name) as media_name,
       -1 as sid,
       'total' as ad_position,
       t20.hour as hour,
       sum(t20.bg_pv) as bg_pv,
       sum(t20.dj_pv) as dj_pv
       from
(select media_id,
       media_name,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
       and if(pFirst_id = -1,1 = 1, media_id=pFirst_id)
)t10
left outer join
(select sid,
        hour,
        bg_pv,
        dj_pv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0' and hour<>'NA'

)t20
on t10.sid= t20.sid
where t20.hour is not null
group by t10.media_id,t20.hour

       
)tt


   
union all


select -1 as first_id,
       'total' as first_name,
       ta.media_id as sub_id,
       ta.media_name as sub_name,
       ta.hour as hour_time,
       ta.bg_pv as bg_pv,
       ta.dj_pv as dj_pv
       from

(
select t1.media_id as media_id,
       min(t1.media_name) as media_name,
       t2.hour as hour,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.dj_pv) as dj_pv
from

(select media_id,
       media_name,
       sid
       from
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
       and if(pFirst_id = -1,1 = 1,pFirst_id = media_id)
)t1
left outer join
(
select sid,
       hour,
       bg_pv,
       dj_pv
       from
       dw_mkt_result_dm
       where
       pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0' and hour<>'NA'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.media_id,t2.hour

union all

select -1 as media_id,
       'total' as media_name,
       t20.hour as hour,
       sum(t20.bg_pv) as bg_pv,
       sum(t20.dj_pv) as dj_pv
from

(select media_id,
       media_name,
       sid
       from
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
       and if(pFirst_id = -1,1 = 1,pFirst_id = media_id)
)t10
left outer join
(
select sid,
       hour,
       bg_pv,
       dj_pv
       from
       dw_mkt_result_dm
       where
       pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0' and hour<>'NA'

)t20
on t10.sid=t20.sid
where t20.hour is not null
group by t20.hour
)ta
)z
where z.first_id=ifnull(pFirst_id,z.first_id)  and z.hour_time is not null
order by img_sub_id DESC,img_hour_time ASC;


-- 小时数据表格展示


if pFirst_id='-1' 
then 


select qq.media_id as tbl_media_id,
       qq.media_name as tbl_media_name,
       qq.channel as tbl_channel,
       qq.sid as tbl_sid,
       qq.ad_position as tbl_ad_position,
       qq.hour as tbl_hour,
       qq.hour_sorted as tbl_hour_sorted,
       qq.bg_pv as tbl_bg_pv,
       qq.bg_uv as tbl_bg_uv,
       qq.dj_pv as tbl_dj_pv,
       qq.dj_uv as tbl_dj_uv
       from
(
select mt.media_id as media_id,
       mt.media_name as media_name,
       mt.channel as channel,
       mt.sid as sid,
       mt.ad_position as ad_position,
       mt.hour as hour,
       mt.hour_sorted as hour_sorted,
       mt.bg_pv as bg_pv,
       mt.bg_uv as bg_uv,
       mt.dj_pv as dj_pv,
       mt.dj_uv as dj_uv
       from

(select 
       min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       min(t1.channel) as channel,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       t2.hour_sorted,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.bg_pv) as bg_uv,
       sum(t2.dj_pv) as dj_pv,
       sum(t2.dj_pv) as dj_uv
       from
(select media_id,
       media_name,
       channel,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
)t1
left outer join
(select sid,
        hour,
        if(hour<>'NA',hour,'0') as hour_sorted,
        bg_pv,
        bg_uv,
        dj_pv,
        dj_uv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.sid,t2.hour,t2.hour_sorted


union all

select tz.media_id as media_id,
       min(tz.media_name) as media_name,
       '-' as channel,
       -1 as sid,
       '媒体总计' as ad_position,
       tz.hour as hour,
       tz.hour_sorted as hour_sorted,
       sum(tz.bg_pv) as bg_pv,
       sum(tz.bg_uv) as bg_uv,
       sum(tz.dj_pv) as dj_pv,
       sum(tz.dj_uv) as dj_uv
       from

(
select min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       min(t1.channel) as channel,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       t2.hour_sorted as hour_sorted,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.bg_pv) as bg_uv,
       sum(t2.dj_pv) as dj_pv,
       sum(t2.dj_pv) as dj_uv
       from
(select media_id,
       media_name,
       channel,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
)t1
left outer join
(select sid,
        hour,
        if(hour<>'NA',hour,'0') as hour_sorted,
        bg_pv,
        bg_uv,
        dj_pv,
        dj_uv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.sid,t2.hour,t2.hour_sorted

)tz
group by tz.media_id ,tz.hour,tz.hour_sorted
)mt
where mt.sid='-1' 

union all


select '-1' as media_id,
       'Total' as media_name,
       '-' as channel,
       '-' as sid,
       '-' as ad_position,
       mz.hour as hour,
       mz.hour_sorted as hour_sorted,
       sum(mz.bg_pv) as bg_pv,
       sum(mz.bg_uv) as bg_uv,
       sum(mz.dj_pv) as dj_pv,
       sum(mz.dj_uv) as dj_uv
       from

(
select mt.media_id as media_id,
       mt.media_name as media_name,
       mt.channel as channel,
       mt.sid as sid,
       mt.ad_position as ad_position,
       mt.hour as hour,
       mt.hour_sorted as hour_sorted,
       mt.bg_pv as bg_pv,
       mt.bg_uv as bg_uv,
       mt.dj_pv as dj_pv,
       mt.dj_uv as dj_uv
       from

(select min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       min(t1.channel) as channel,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       t2.hour_sorted as hour_sorted,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.bg_pv) as bg_uv,
       sum(t2.dj_pv) as dj_pv,
       sum(t2.dj_pv) as dj_uv
       from
(select media_id,
       media_name,
       channel,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
)t1
left outer join
(select sid,
        hour,
        if(hour<>'NA',hour,'0') as hour_sorted,
        bg_pv,
        bg_uv,
        dj_pv,
        dj_uv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.sid,t2.hour,t2.hour_sorted

union all

select tz.media_id as media_id,
       min(tz.media_name) as media_name,
       '-' as channel,
       -1 as sid,
       '媒体总计' as ad_position,
       tz.hour as hour,
       tz.hour_sorted as hour_sorted,
       sum(tz.bg_pv) as bg_pv,
       sum(tz.bg_uv) as bg_uv,
       sum(tz.dj_pv) as dj_pv,
       sum(tz.dj_uv) as dj_uv
       from

(
select min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       min(t1.channel) as channel,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       t2.hour_sorted,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.bg_pv) as bg_uv,
       sum(t2.dj_pv) as dj_pv,
       sum(t2.dj_pv) as dj_uv
       from
(select media_id,
       media_name,
       channel,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
)t1
left outer join
(select sid,
        hour,
        if(hour<>'NA',hour,'0')    as hour_sorted,
        bg_pv,
        bg_uv,
        dj_pv,
        dj_uv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.sid,t2.hour,t2.hour_sorted

)tz
group by tz.media_id ,tz.hour,tz.hour_sorted
)mt
where mt.sid='-1'
)mz

group by mz.hour,mz.hour_sorted
)qq
where qq.hour is not null
order by tbl_media_id ASC,tbl_hour_sorted ASC;

else 


select mm.media_id as tbl_media_id,
       mm.media_name as tbl_media_name,
       mm.channel as tbl_channel,
       mm.sid as tbl_sid,
       mm.ad_position as tbl_ad_position,
       mm.hour as tbl_hour,
       mm.hour_sorted as tbl_hour_sorted,
       mm.bg_pv as tbl_bg_pv,
       mm.bg_uv as tbl_bg_uv,
       mm.dj_pv as tbl_dj_pv,
       mm.dj_uv as tbl_dj_uv
       from

(select min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       min(t1.channel) as channel,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       t2.hour_sorted as hour_sorted,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.bg_pv) as bg_uv,
       sum(t2.dj_pv) as dj_pv,
       sum(t2.dj_pv) as dj_uv
       from
(select media_id,
       media_name,
       channel,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
)t1
left outer join
(select sid,
        hour,
        if(hour<>'NA',hour,'0') as hour_sorted,
        bg_pv,
        bg_uv,
        dj_pv,
        dj_uv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.sid,t2.hour,t2.hour_sorted

union all

select tz.media_id as media_id,
       min(tz.media_name) as media_name,
       '-' as channel,
       -1 as sid,
       '媒体总计' as ad_position,
       tz.hour as hour,
       tz.hour_sorted as hour_sorted,
       sum(tz.bg_pv) as bg_pv,
       sum(tz.bg_uv) as bg_uv,
       sum(tz.dj_pv) as dj_pv,
       sum(tz.dj_uv) as dj_uv
       from

(
select min(t1.media_id) as media_id,
       min(t1.media_name) as media_name,
       min(t1.channel) as channel,
       t1.sid as sid,
       min(t1.ad_position) as ad_position,
       t2.hour as hour,
       t2.hour_sorted as hour_sorted,
       sum(t2.bg_pv) as bg_pv,
       sum(t2.bg_pv) as bg_uv,
       sum(t2.dj_pv) as dj_pv,
       sum(t2.dj_pv) as dj_uv
       from
(select media_id,
       media_name,
       channel,
       sid,
       ad_position
       from 
       dim_mkt_base_info_ds
       where 
       activtiy_id = ifnull(pActivtiy_id,activtiy_id) and if(pMedia_type=0 ,media_type in (0),if(pMedia_type=1,media_type in (1,2),media_type in (0,1,2)))
)t1
left outer join
(select sid,
        hour,
        if(hour<>'NA',hour,'0') as hour_sorted,
        bg_pv,
        bg_uv,
        dj_pv,
        dj_uv
        from
        dw_mkt_result_dm
        where
        pt_d = ifnull(tmpPt_d,pt_d) and area_type='0' and user_type='0'

)t2
on t1.sid=t2.sid
where t2.hour is not null
group by t1.sid,t2.hour,t2.hour_sorted

)tz
group by tz.media_id ,tz.hour,tz.hour_sorted
)mm
where mm.media_id = ifnull(pFirst_id,mm.media_id) and mm.hour is not null
order by tbl_sid ASC ,tbl_hour_sorted ASC;

end if; 

end
   
   //
DELIMITER ;

   