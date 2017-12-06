drop procedure prepare_group;
DELIMITER $$
CREATE PROCEDURE `prepare_group`(in dest_table varchar(255),
   in trt varchar(255),
   in crt varchar(255),
   in group_id bigint,
   in use_date datetime,
   in neighbor_dates varchar(255),
   in limit_num bigint)
begin
   declare msql text;
   -- ��crt�������˺�ķ������� 
   set msql=concat('insert into `',crt,'` select a.imei,''',use_date,''' as use_date,',group_id,' as reserve_id from ',trt,' a left join `',crt,'` b on a.imei = b.imei  group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,';');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql; 
   -- ��ȡcrt�еĶ�Ӧ��������ݵ����յĽ�����ݼ�����
   set msql=concat('insert into `',dest_table,'` select imei as device_id from `',crt,'` where reserve_id=',group_id,';');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql; 
end;
$$



drop procedure prepare_group_query;
DELIMITER $$
CREATE PROCEDURE `prepare_group_query`(in dest_table varchar(255),
   in trt varchar(255),
   in crt varchar(255),
   in group_id bigint,
   in use_date datetime,
   in neighbor_dates varchar(255),
   in limit_num bigint,out destTableCount bigint)
begin
   declare msql text;
   -- ����������ʱ��
   set msql=concat('create TEMPORARY  table `odp_tmp_',group_id,'`(imei varchar(255),primary key(`imei`))ENGINE = innodb ','select a.imei from ',trt,' a left join `',crt,'` b on a.imei = b.imei  group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,';');
   set @qsql=msql;
   PREPARE msql from @qsql;
	 EXECUTE  msql;

      -- ɾ����ʱ��
   set msql=concat('select count(*) into @destTableCount from `odp_tmp_',group_id,'`;');
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
   set destTableCount=@destTableCount;

   -- ɾ����ʱ��
   set msql=concat('drop table if exists `odp_tmp_',group_id,'`;');
   set @qsql=msql;
   -- select @qsql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
end;
$$
