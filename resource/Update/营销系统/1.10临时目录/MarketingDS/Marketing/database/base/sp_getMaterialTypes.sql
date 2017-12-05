DROP PROCEDURE IF EXISTS sp_getMaterialTypes;
CREATE PROCEDURE sp_getMaterialTypes (
    out pRetCode            int,
    IN pAccount            VARCHAR(255)
)
proc: BEGIN
    SELECT 0 INTO pRetCode;
    SELECT dic_key AS materialId,dic_value AS materialName FROM t_mkt_common_dic_info WHERE TYPE = 'material';
end;