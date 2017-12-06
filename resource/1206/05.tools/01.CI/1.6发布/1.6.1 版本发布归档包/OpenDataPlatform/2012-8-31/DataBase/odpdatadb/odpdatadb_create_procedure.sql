DROP PROCEDURE IF EXISTS `prepare_group`;
create procedure `prepare_group`
(
   in dest_table varchar(255),
   in trt varchar(255),
   in crt varchar(255),
   in group_id bigint,
   in use_date datetime,
   in neighbor_dates varchar(255),
   in limit_num bigint
)
begin
   declare msql text;
   -- 向crt表插入过滤后的分组数据
   set msql=concat('insert into `',crt,'` select a.imei,''',use_date,''' as use_date,',group_id,' as reserve_id from ',trt,' a left join `',crt,'` b on a.imei = b.imei  where a.dest_table = ''default'' group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,';');
   -- 提取crt中的对应分组的数据到最终的结果数据集合中
   set msql=concat(msql,'insert into `',dest_table,'` select imei as device_id from `',crt,'` where reserve_id=',group_id,';');
   -- 从trt中删除(更新)最终的结果集数据
   set msql=concat(msql,'update table `',trt,'`set dest_table=''',dest_table,''' where imei in (select device_id from ',dest_table,');');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql; 
end



DROP PROCEDURE IF EXISTS `prepare_group_query`;

CREATE DEFINER = `root`@`%` PROCEDURE `prepare_group_query`(in dest_table varchar(255),
   in trt varchar(255),
   in crt varchar(255),
   in group_id bigint,
   in use_date datetime,
   in neighbor_dates varchar(255),
   in limit_num bigint)
begin
   declare msql text;
   --  更新tmp_result_table数据
   set msql=concat('update `',trt,'` set dest_table=''',dest_table,''' where imei in (select * from (select a.imei from ',trt,' a left join `',crt,'` b on a.imei = b.imei  where a.dest_table = ''default'' group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,')c);');
   set @qsql=msql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
end;