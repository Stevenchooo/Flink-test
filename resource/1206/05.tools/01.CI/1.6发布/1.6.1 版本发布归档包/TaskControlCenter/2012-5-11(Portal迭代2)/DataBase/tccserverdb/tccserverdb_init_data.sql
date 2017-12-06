set @@autocommit=0;
drop procedure if exists initTaskIdStepIdSerial;
delimiter //
create procedure initTaskIdStepIdSerial()
begin
  	delete from tcc_taskid_serial;
	insert into tcc_taskid_serial select serviceId,taskType,max(serialNo) as serialNo  from (select floor(task_id/1000000) as serviceId,floor(task_id/10000)%100 as taskType,task_id%10000 as serialNo from tcc_task) temp group by serviceId,taskType;
	delete from tcc_stepid_serial;
	insert into tcc_stepid_serial select task_id,max(step_id) as serialNo from tcc_task_step group by task_id; 
end
//
delimiter ;
call initTaskIdStepIdSerial();
drop procedure if exists initTaskIdStepIdSerial;
set @@autocommit=1;