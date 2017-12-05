delimiter //
DROP PROCEDURE IF EXISTS sp_roleQuery;
CREATE PROCEDURE sp_roleQuery (
    out pRetCode            int,
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
	set pRetCode = 0;

	select role,name,description as 'desc' from t_ms_role limit pFrom, pPerPage;
	select count(*) as total from t_ms_role;
end//