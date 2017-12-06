set @@autocommit=0;
drop procedure if exists initTaskStepIdSerial;
create procedure initTaskStepIdSerial()
begin
  	delete from tcc_taskid_servial;
	insert into tcc_taskid_servial select floor(task_id/1000000) as serviceId,floor(task_id/10000)%100 as taskType,max(task_id%10000) as serialNo from tcc_task group by serviceId,taskType; 
	delete from tcc_stepid_serial;
	insert into tcc_stepid_serial select task_id,max(step_id) as serialNo from tcc_task_step group by task_id; 
end;
call initTaskStepIdSerial();
drop procedure if exists initTaskStepIdSerial;
commit;

set @@autocommit=1;