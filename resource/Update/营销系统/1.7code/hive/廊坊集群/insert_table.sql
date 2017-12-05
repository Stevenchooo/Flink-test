delete from dim_mkt_base_info_ds;
insert into  dim_mkt_base_info_ds(activtiy_id,activity_name,media_id,media_name,sid,begin_date,end_date,
                                  estimate_exp,estimate_click,media_type,url_host,landing_plate,resourceName,
                                  productName,cid,operator,state,channel,ad_position)
select * from 
(
    select tt.id                        as activtiy_id,        
           tt.name                      as activity_name,      
           t2.id                        as media_id,           
           t2.name                      as media_name,         
           t7.bi_code                   as sid,                
           date_format(tt.delivery_start_time,'%Y%m%d')       as begin_date,         
           date_format(tt.delivery_end_time,'%Y%m%d')         as end_date,           
           t1.exp_amount                as estimate_exp,       
           t1.click_amount              as estimate_click,     
           t5.dic_value                 as media_type,         
           t10.land_link                as url_host,           
           fk.dic_value                 as landing_plate,      
           t8.name                      as resourceName,       
           t3.productName               as productName,        
           t10.cid                      as cid,
           t1.operator                  as operator,
           t1.state                     as state,
           t1.channel                   as channel,
           t1.ad_position               as ad_position              
    from 
    (
      select
            *
      from
      t_mkt_ad_info
    ) t1
    left outer join
    (
      select
            dic_key     as  id,
            dic_value   as  name
      from
            t_mkt_common_dic_info
      where
            type = 'resource'
                
    )t8
    on t1.resource = t8.id
    left outer join
    (
        select
            f.id    as id,
            f.name  as name,
            k.name  as productName
        from 
        (
            select
                id,
                name,
                product
            from 
            t_mkt_info 
        ) f
        left outer join
        (
            select
                id,
                name
            from t_mkt_dic_info
            where type = 'product'                
        ) k
        on f.product = k.id
        
     ) t3
    on t1.id = t3.id
    left outer join
    (
      select
       dic_key     as  id,
       dic_value
      from
       t_mkt_common_dic_info
      where
       type = 'land_platform'
   )fk
   on t1.platform = fk.id
   left outer join
   (
      select
        *
      from
       t_mkt_info
   )tt
   on tt.id = t1.id
   left outer join
   (
      select
       id,
       name
      from
       t_mkt_dic_info
      where
       type = 'web'
   )t2
   on t1.web_name = t2.id
   left outer join
   (
      select
       dic_key     as  id,
       dic_value
      from
       t_mkt_common_dic_info
      where
       type = 'port'
   )t5
   on t1.port = t5.id
   left outer join
   (
      select
    *
      from
       t_mkt_monitor_info
   )t7
   on t1.aid = t7.aid
   left outer join
   t_mkt_land_info t10
   on t1.aid = t10.aid
   where t7.bi_code is not null
   ) c;
