
DROP PROCEDURE IF EXISTS `reserveSequence`;
delimiter ;;
CREATE PROCEDURE `reserveSequence`(tblNum int,
     partNum int)
begin
   START TRANSACTION; 

   update sequence_table set NEXT_VAL=NEXT_VAL+
         case(SEQUENCE_NAME)
            when 'org.apache.hadoop.hive.metastore.model.MPartition' then partNum
            when 'org.apache.hadoop.hive.metastore.model.MSerDeInfo' then partNum+tblNum
						when 'org.apache.hadoop.hive.metastore.model.MStorageDescriptor' then partNum+tblNum
            when 'org.apache.hadoop.hive.metastore.model.MTable' then tblNum
						else 0 end;
   select max(if(SEQUENCE_NAME='org.apache.hadoop.hive.metastore.model.MPartition',next_val,0)) as pId,
          max(if(SEQUENCE_NAME='org.apache.hadoop.hive.metastore.model.MTable',next_val,0)) as tId,
					max(if(SEQUENCE_NAME='org.apache.hadoop.hive.metastore.model.MSerDeInfo',next_val,0)) as serId,
					max(if(SEQUENCE_NAME='org.apache.hadoop.hive.metastore.model.MStorageDescriptor',next_val,0)) as sdId from sequence_table;
 
   commit;
end;;

delimiter ;


DROP PROCEDURE IF EXISTS `deleteTblRelations`;
delimiter ;;
CREATE  PROCEDURE `deleteTblRelations`(tblIds text)
begin
		declare msql text; 
		start transaction;
		drop table if exists tmp_tIds;
		drop table if exists tmp_pIds;
		drop table if exists tmp_sdIds;
		drop table if exists tmp_serIds;
		create TEMPORARY  table `tmp_tIds`(tbl_id bigint,primary key(`tbl_id`))ENGINE = HEAP;
		create TEMPORARY  table `tmp_pIds`(part_id bigint,primary key(`part_id`))ENGINE = HEAP;
		create TEMPORARY  table `tmp_sdIds`(sd_id bigint,primary key(`sd_id`))ENGINE = HEAP;
		create TEMPORARY  table `tmp_serIds`(serde_id bigint,primary key(`serde_id`))ENGINE = HEAP;

		set @sql=concat('insert into tmp_tIds values',tblIds,';');
		PREPARE msql from @sql;
		EXECUTE  msql; 
		insert into tmp_pIds select a.part_id from partitions a,tmp_tIds b where a.tbl_id=b.tbl_id;
		insert into tmp_sdIds select a.sd_id from partitions a,tmp_pIds b where a.part_id=b.part_id;
		insert into tmp_sdIds select a.sd_id from tbls a,tmp_tIds b where a.tbl_id=b.tbl_id;
		insert into tmp_serIds select a.serde_id from sds a,tmp_sdIds b where a.sd_id=b.sd_id;


		delete  a from partition_key_vals a,tmp_pIds b where a.part_id=b.part_id;
		delete  a from partition_params a,tmp_pIds b where a.part_id=b.part_id;
		delete  a from partitions a,tmp_pIds b where a.part_id=b.part_id;
		drop table tmp_pIds;
	
		delete  a from table_params a,tmp_tIds b where a.tbl_id=b.tbl_id;
		delete  a from partition_keys a,tmp_tIds b where a.tbl_id=b.tbl_id;
		delete  a from tbl_privs a,tmp_tIds b where a.tbl_id=b.tbl_id;
		delete  a from tbls a,tmp_tIds b where a.tbl_id=b.tbl_id;
		drop table tmp_tIds;	

		delete  a from columns a,tmp_sdIds b where a.sd_id=b.sd_id;
		delete  a from sds a,tmp_sdIds b where a.sd_id=b.sd_id;
		drop table tmp_sdIds;

		delete  a from serde_params a,tmp_serIds b where a.serde_id=b.serde_id;
		delete  a from serdes a,tmp_serIds b where a.serde_id=b.serde_id;
		drop table tmp_serIds;

commit;
end;;

delimiter ;