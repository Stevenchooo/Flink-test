drop PROCEDURE if exists p_tcc_getTaskserialno;
delimiter //
CREATE PROCEDURE p_tcc_getTaskserialno(IN serviceIDin int,IN taskTypein int, OUT taskserialno int)
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
END
//
delimiter ;

drop PROCEDURE if exists p_tcc_getTaskStepserialno;
delimiter //
CREATE PROCEDURE p_tcc_getTaskStepserialno(IN `taskId` bigint,OUT `stepSerialno` int)
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
END
//
delimiter ;

drop FUNCTION if exists removeDependTaskId;
delimiter //
CREATE FUNCTION removeDependTaskId(dependTaskIdList TEXT,taskId BIGINT) RETURNS TEXT
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
END
//
delimiter ;