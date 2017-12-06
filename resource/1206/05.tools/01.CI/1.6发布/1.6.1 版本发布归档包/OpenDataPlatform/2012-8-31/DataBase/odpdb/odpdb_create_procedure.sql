delimiter ;
DROP PROCEDURE IF EXISTS `prepare_group`;
delimiter //
CREATE PROCEDURE `prepare_group`(
   in dest_table varchar(255),
   in trt varchar(255),
   in crt varchar(255),
   in group_id bigint,
   in use_date datetime,
   in neighbor_dates varchar(255),
   in limit_num bigint)
begin
   declare msql text;
   -- 向crt表插入过滤后的分组数据 
   set msql=concat('insert into `',crt,'` select a.imei,''',use_date,''' as use_date,',group_id,' as reserve_id from ',trt,' a left join `',crt,'` b on a.imei = b.imei  where a.dest_table = ''default'' group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,';');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql; 
   -- 提取crt中的对应分组的数据到最终的结果数据集合中
   set msql=concat('insert into `',dest_table,'` select imei as device_id from `',crt,'` where reserve_id=',group_id,';');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql; 
   -- 从trt中删除(更新)最终的结果集数据
   set msql=concat('update `',trt,'`set dest_table=''',dest_table,''' where imei in (select device_id from ',dest_table,');');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql; 
end
//
delimiter ;

DROP PROCEDURE IF EXISTS `prepare_group_query`;
delimiter //
CREATE PROCEDURE `prepare_group_query`(in dest_table varchar(255),
   in trt varchar(255),
   in crt varchar(255),
   in group_id bigint,
   in use_date datetime,
   in neighbor_dates varchar(255),
   in limit_num bigint)
begin
   declare msql text;
   -- 设置转储阈值为64M 
   set msql='set session tmp_table_size=256*1024*1024;';
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
   -- 设置临时表空间大小为64M
   set msql='set session max_heap_table_size=1024*1024*1024;';
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
   -- 创建过滤临时表
   set msql=concat('create TEMPORARY  table `odp_tmp_',group_id,'`(imei varchar(255),primary key(`imei`))ENGINE = HEAP ','select a.imei from ',trt,' a left join `',crt,'` b on a.imei = b.imei  where a.dest_table = ''default'' group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,';');
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
   --  更新tmp_result_table数据 
   set msql=concat('update `',trt,'` set dest_table=''',dest_table,''' where imei in (select * from `odp_tmp_',group_id,'`);');
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
   -- 删除临时表
   set msql=concat('drop table if exists `odp_tmp_',group_id,'`;');
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
end
//

