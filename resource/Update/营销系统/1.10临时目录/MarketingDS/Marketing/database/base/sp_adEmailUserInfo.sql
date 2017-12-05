DELIMITER // 
DROP PROCEDURE IF EXISTS sp_adEmailUserInfo;
CREATE PROCEDURE sp_adEmailUserInfo (
    out pRetCode            int,
    in  mktId               int
)
proc: BEGIN
   
    
     declare vmallFlag        int;
     declare honorFlag        int;
     declare huaweiFlag       int;
     declare other            int;
     
     
     declare vType            int;
     SELECT 0 INTO pRetCode;
    
     select dept_type into vType from t_mkt_info where id = mktId;
     
    if(exists(select * from t_mkt_ad_info where id=mktId and  platform = '0')) then
        select 1 into vmallFlag;
    end if;
    
    
    if(exists(select * from t_mkt_ad_info where id=mktId and  platform = '1')) then
        select 1 into honorFlag;
    end if;
    
    if(exists(select * from t_mkt_ad_info where id=mktId and  platform = '2')) then
        select 1 into huaweiFlag;
    end if;
    
    if(exists(select * from t_mkt_ad_info where id=mktId and  platform in ('3','4','5'))) then
        select 1 into other;
    end if;
    
    select
        t3.account               as      account,
        t3.name                  as      name,
        t3.department            as      department,
        t4.role                  as      role,
        if(t4.role = 'ad_vmall' and vmallFlag = 1,1,if(t4.role = 'ad_honor' and honorFlag = 1,1,if(t4.role='ad_huawei' and huaweiFlag = 1,1,if(t4.role='ad_other' and other = 1,1,0))))    as   selectFlag
    from
    (
        select
            t2.name         as      name,
            max(t2.val)     as      role
        from
        (
           select
                distinct role
           from
               t_ms_right
           where
                meta in ('mktLandInfo' ,'mktLandHonor','mktLandForOther')  and  (oprRight like '%|c|%'  or oprRight like '%|u|%') 
        )t1
        join
        (
            select
               distinct name,val
            from 
                t_ms_model 
             where meta = 'admin' and val is not null  and name in (select account from t_ms_user where deptType = vType)
        )t2
        on t1.role = t2.val
        
        group by t2.name
    )t4
    join
        t_ms_user t3
    on t4.name = t3.account
    
    order by selectFlag desc ,role,account;
    
end

   //
DELIMITER ;