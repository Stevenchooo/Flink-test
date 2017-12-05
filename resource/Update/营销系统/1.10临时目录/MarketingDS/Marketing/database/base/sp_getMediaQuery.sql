DROP PROCEDURE IF EXISTS sp_getMediaQuery;
CREATE PROCEDURE sp_getMediaQuery (
    out pRetCode            int,
    IN pAccount            VARCHAR(255)
)
proc: BEGIN
      DECLARE vType           INT;
    
	    SET vType = NULL;
	    
	    IF(NOT EXISTS(SELECT * FROM t_ms_model WHERE meta = 'admin' AND val = 'root' AND NAME = pOperator)) THEN
		SELECT deptType INTO vType FROM t_ms_user WHERE account = pOperator;
	    END IF;
    
    
    
    
     SELECT 0 INTO pRetCode;
     
     
     SELECT t1.name AS webName,t1.id AS webId,t2.name AS mediaName 
     FROM
     (SELECT * FROM t_mkt_dic_info WHERE TYPE='web' AND dept_type = IFNULL(vType,dept_type)) t1
     JOIN 
     (SELECT  id,NAME FROM  t_mkt_dic_info WHERE  TYPE='media' AND dept_type = IFNULL(vType,dept_type) ) t2  ON t1.pid=t2.id
     GROUP BY webName,mediaName
     ORDER BY mediaName;
end;