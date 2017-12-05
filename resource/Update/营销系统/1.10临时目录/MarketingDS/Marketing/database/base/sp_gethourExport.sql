DELIMITER // 
DROP PROCEDURE IF EXISTS sp_gethourExport;
CREATE PROCEDURE sp_gethourExport (
     out pRetCode               int,
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
-- 小时数据表格展示

if pFirst_id='-1' 
then 


select qq.media_name as mediaName,
       qq.channel as mktLandInfoChannel,
       qq.sid as mktLandInfoSID,
       qq.ad_position as mktLandInfoAdPosition,
       qq.hour as hour,
       qq.bg_pv as bgPv,
       qq.bg_uv as bgUv,
       qq.dj_pv as djPv,
       qq.dj_uv as djUv,
       if(qq.bg_pv = 0, 0,format(qq.dj_pv/qq.bg_pv,2)) as ctr
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
group by t1.sid,t2.hour,t2.hour_sorted

)tz
group by tz.media_id ,tz.hour,tz.hour_sorted
)mt
where mt.sid='-1'
)mz

group by mz.hour,mz.hour_sorted
)qq
where qq.hour is not null
order by qq.media_id ASC,qq.hour_sorted ASC;

else 


select mm.media_name as mediaName,
       mm.channel as mktLandInfoChannel,
       mm.sid as mktLandInfoSID,
       mm.ad_position as mktLandInfoAdPosition,
       mm.hour as hour,
       mm.bg_pv as bgPv,
       mm.bg_uv as bgUv,
       mm.dj_pv as djPv,
       mm.dj_uv as djUv,
       if(mm.bg_pv = 0, 0,format(mm.dj_pv/mm.bg_pv,2)) as ctr
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
group by t1.sid,t2.hour,t2.hour_sorted

)tz
group by tz.media_id ,tz.hour,tz.hour_sorted
)mm
where mm.media_id = ifnull(pFirst_id,mm.media_id) and mm.hour is not null
order by mm.sid ASC ,mm.hour_sorted ASC;

end if; 
end
   
   //
DELIMITER ;

   