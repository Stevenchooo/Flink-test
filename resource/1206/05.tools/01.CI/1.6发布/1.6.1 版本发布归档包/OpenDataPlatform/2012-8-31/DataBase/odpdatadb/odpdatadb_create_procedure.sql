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
   -- ��crt�������˺�ķ�������
   set msql=concat('insert into `',crt,'` select a.imei,''',use_date,''' as use_date,',group_id,' as reserve_id from ',trt,' a left join `',crt,'` b on a.imei = b.imei  where a.dest_table = ''default'' group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,';');
   -- ��ȡcrt�еĶ�Ӧ��������ݵ����յĽ�����ݼ�����
   set msql=concat(msql,'insert into `',dest_table,'` select imei as device_id from `',crt,'` where reserve_id=',group_id,';');
   -- ��trt��ɾ��(����)���յĽ��������
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
   --  ����tmp_result_table����
   set msql=concat('update `',trt,'` set dest_table=''',dest_table,''' where imei in (select * from (select a.imei from ',trt,' a left join `',crt,'` b on a.imei = b.imei  where a.dest_table = ''default'' group by a.imei having count(b.use_date) <2 and sum(if(b.use_date in ',neighbor_dates,',1,0)) =0  limit ',limit_num,')c);');
   set @qsql=msql;
   PREPARE msql from @qsql;
   EXECUTE  msql;
end;