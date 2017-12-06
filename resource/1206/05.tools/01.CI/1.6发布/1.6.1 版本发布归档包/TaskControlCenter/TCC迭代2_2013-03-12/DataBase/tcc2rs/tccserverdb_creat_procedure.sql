use tcc2rs;

DELIMITER ;;
CREATE FUNCTION `removeDependTaskId`(dependTaskIdList TEXT,taskId BIGINT) RETURNS text CHARSET utf8
BEGIN
  DECLARE removedDependTaskIdList TEXT;
  DECLARE startIndex INT;
  DECLARE taskIDStr TEXT;
  SET taskIDStr = CONCAT(CONCAT(';',taskId),',');
  SET removedDependTaskIdList = '';
  SET dependTaskIdList = CONCAT(';',dependTaskIdList);
  WHILE LENGTH(dependTaskIdList) > 0 DO
SET startIndex = LOCATE(taskIDStr,dependTaskIdList);
IF startindex != 0
THEN
SET removedDependTaskIdList = CONCAT(removedDependTaskIdList,LEFT(dependTaskIdList,startIndex-1));
                        IF ',' = SUBSTRING(dependTaskIdList,startindex + LENGTH(taskIDStr)+3,1)
THEN
SET dependTaskIdList = SUBSTRING(dependTaskIdList,startindex + LENGTH(taskIDStr)+5);
ELSE
SET dependTaskIdList = SUBSTRING(dependTaskIdList,startindex + LENGTH(taskIDStr)+3);
                        END IF;

ELSE
SET removedDependTaskIdList = CONCAT(removedDependTaskIdList, dependTaskIdList);
SET dependTaskIdList = '';
END IF;
  END WHILE;
  IF LENGTH(removedDependTaskIdList) > 0
  THEN
SET removedDependTaskIdList = SUBSTRING(removedDependTaskIdList,2);
  END IF;
  RETURN(removedDependTaskIdList);
END ;;

DELIMITER ;
DELIMITER ;;
CREATE PROCEDURE `export_task_running_info`(
    
)
begin
   declare msql text;
   declare pre_date_str varchar(255);
   declare curr_date_str varchar(255);
   declare curr_time_str varchar(255);
   declare curr_time timestamp;
   set curr_time = now();
   
   set pre_date_str = date_format(date_add(curr_time,interval -1 day),'%Y-%m-%d 08:00:00');
   
   set curr_date_str = date_format(curr_time,'%Y-%m-%d 08:00:00');
   set curr_time_str = date_format(curr_time,'%Y-%m-%d_%H:%i:%s');  
   
   set msql=concat('select ''57'' as db_id,f.*,date(start_time) as run_date,unix_timestamp(f.end_time) - unix_timestamp(f.start_time) as execute_seconds from
(select b.service_name,
       a.task_name,
   c.cycle_id as period_id,
   c.state,
   min(e.Running_Start_Time) as start_time,
   c.Running_End_Time as end_time,
   group_concat(e.Running_Job_ID) as job_Ids,
   group_concat(d.Job_Input) as file_names,
   a.serviceid as service_id,
   c.task_id,
   a.Multi_Batch_Flag as ods_flag
from tcc_task a,
     service_defination b,
 tcc_task_running_state c,
 tcc_batch_running_state d,
 tcc_step_running_state e
where a.task_id = c.task_id and
      a.serviceid=b.service_id and
      c.task_id = d.task_id and
  c.cycle_id =  d.cycle_id and
  d.task_id = e.task_id and
  d.cycle_id =  e.cycle_id and
  d.batch_id = e.batch_id and
  c.Running_Start_Time>=''',pre_date_str,'''  and
  c.Running_Start_Time<''',curr_date_str,'''
group by c.task_id,c.cycle_id
order by start_time) f
into outfile  ''/home/mysql/tasks/task_instance_',curr_time_str,'.info_57''');
   
   set @sql = msql;
   prepare msql from @sql;
   execute msql;
end ;;


DELIMITER ;
DELIMITER ;;
CREATE PROCEDURE `getDuplicateNameTasks`(idNames text)
begin
   declare msql text;
   drop table if exists temp_task_id_name;
   create temporary table temp_task_id_name(task_id bigint,task_name varchar(200));  
   set msql=concat('insert into temp_task_id_name values',idNames,';');
   set @sql=msql;
   PREPARE msql from @sql;
   EXECUTE  msql;
   select a.task_id from temp_task_id_name a, tcc_task b where a.task_name = b.task_name and a.task_id != b.task_id;
end ;;


DELIMITER ;
DELIMITER ;;
CREATE PROCEDURE `p_tcc_getTaskserialno`(IN serviceIDin int,IN taskTypein int, OUT taskserialno int)
BEGIN
DECLARE defaule_serialno_value int(1) DEFAULT 0;
DECLARE count INT;
START TRANSACTION;
select count(*) into count from tcc_taskid_serial where serviceID=serviceIDin and taskType=taskTypein FOR UPDATE;
if(count = 0) then
insert into tcc_taskid_serial (serviceID,taskType,serialNo) values (serviceIDin,taskTypein,defaule_serialno_value);
end if;

UPDATE tcc_taskid_serial SET serialNo = LAST_INSERT_ID(serialNo + 1) where serviceID=serviceIDin and taskType=taskTypein;
SELECT LAST_INSERT_ID() into taskserialno;

COMMIT;
END ;;


DELIMITER ;
DELIMITER ;;
CREATE PROCEDURE `p_tcc_getTaskStepserialno`(IN `taskId` bigint,OUT `stepSerialno` int)
BEGIN
DECLARE default_serialno_value int(1) DEFAULT 0;
DECLARE count INT;
START TRANSACTION;
select count(*) into count from tcc_stepid_serial where task_Id=taskId FOR UPDATE;
if(count = 0) then
   insert into tcc_stepid_serial (task_Id,serialNo) values (taskId,default_serialno_value);
end if;

UPDATE tcc_stepid_serial SET serialNo = LAST_INSERT_ID(serialNo + 1) where task_Id=taskId;
SELECT LAST_INSERT_ID() into stepSerialno;

COMMIT;
END ;;
DELIMITER ;


drop procedure if exists `maxCycleId`;
delimiter ;;
create function maxCycleId(cycleOffSet varchar(50),curr DateTime) returns varchar(50)
begin
        declare j int default 1;
       declare Mon int default 0;
			 declare D int default 0;
       declare h int default 0;
       declare min int default 0;
       declare c varchar(1);
       declare i int default 1;
   if isnull(cycleOffSet) or trim(cycleOffSet) = '0'  or trim(cycleOffSet) = ''
   then
       return DATE_FORMAT(curr ,'%Y%m%d-%H');
   else
       while j <= length(cycleOffSet) do
          set c = mid(cycleOffSet,j,1);
          if ascii(c) = ascii('M') then
										set Mon=cast(trim(substring(cycleOffSet,i,j-i)) as SIGNED);
										set i = j+1;
					else if ascii(c) = ascii('D') then
										set D=cast(trim(substring(cycleOffSet,i,j-i)) as SIGNED);
										set i = j+1;
					else if ascii(c) =  ascii('h') then
										set h=cast(trim(substring(cycleOffSet,i,j-i)) as SIGNED);
										set i = j+1;
					else if ascii(c) =  ascii('m') then
										set min=cast(trim(substring(cycleOffSet,i,j-i)) as SIGNED);
										set i = j+1;
          end if;
          end if;
          end if;
          end if;
          set j = j + 1;
       end while;
       return  DATE_FORMAT(DATE_ADD(DATE_ADD(curr,INTERVAL Mon MONTH),INTERVAL D*24*60+h*60+min MINUTE) ,'%Y%m%d-%H');
   end if;
end;;

DELIMITER ;

drop procedure if exists `minCycleId`;
delimiter ;;
create function minCycleId(startTime DateTime,bench DateTime) returns varchar(50)
begin
   if not isnull(startTime) and startTime >= bench  then
       if minute(startTime) > 0 or second(startTime) > 0 then
          return DATE_FORMAT(DATE_ADD(startTime,INTERVAL 1 HOUR) ,'%Y%m%d-%H');
       else
					return DATE_FORMAT(startTime ,'%Y%m%d-%H');
       end if;
   else
       return DATE_FORMAT(bench ,'%Y%m%d-%H');
   end if;
end;;

DELIMITER ;

drop procedure if exists `getTopOldNeedRunCycles`;
delimiter ;;
create PROCEDURE `getTopOldNeedRunCycles`
(
  exclude_taskIds text,
  bench DateTime,
  curr DateTime
)
begin
declare msql text; 
start transaction;
  drop table if exists task_min_max_cycleId;
	create temporary table task_min_max_cycleId(task_id bigint, min_cycle_id varchar(100),max_cycle_id varchar(100),primary key(`task_id`))ENGINE = HEAP; 
  insert into task_min_max_cycleId select task_id,minCycleId(start_time,bench) min_cycle_id, maxCycleId(cycle_offset,curr) max_cycle_id from tcc_task where cycle_depend_flag=1 and Task_Enable_Flag=1 and Task_State=0;
	if not (isnull(exclude_taskIds) or trim(exclude_taskIds) = '') then
     set @sql=concat('delete from task_min_max_cycleId where task_id in (',exclude_taskIds,');');
	   PREPARE msql from @sql;
	   EXECUTE  msql; 
  end if;
	select b.task_id,min(b.cycle_id) as cycle_id,b.state,b.running_start_time,b.running_end_time,b.has_alarm_latest_start,b.return_times,b.node_sequence from task_min_max_cycleId a,tcc_task_running_state b where a.task_id=b.task_id and b.cycle_id>=a.min_cycle_id and b.cycle_id<=a.max_cycle_id and b.state in (0,1,12) group by b.task_id;
  drop table if exists task_min_max_cycleId;
commit;
end;;

DELIMITER ;

drop procedure if exists `getDependsOkCycleNums`;
delimiter ;;
create procedure `getDependsOkCycleNums`
(
   depends LONGTEXT
)
begin
declare msql LONGTEXT; 
drop table if exists cycle_depends;
create temporary table cycle_depends(task_id bigint, cycle_id varchar(100),depend_task_id bigint,depend_cycle_id varchar(100),depend_state int)ENGINE = HEAP; 
if not (isnull(depends) or trim(depends) = '') then
     set @sql=concat('insert into cycle_depends values',depends,';');
	   PREPARE msql from @sql;
	   EXECUTE  msql; 
end if;
select b.task_id,b.cycle_id,count(*) as exist_num from tcc_task_running_state a,cycle_depends b where a.task_id=b.depend_task_id and a.cycle_id=b.depend_cycle_id and a.state=b.depend_state group by b.task_id,b.cycle_id;
drop table if exists cycle_depends;
end;;

DELIMITER ;