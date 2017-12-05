

DROP PROCEDURE IF EXISTS sp_adInfodelMaterial;

CREATE PROCEDURE sp_adInfodelMaterial (
    out pRetCode            int,
    in pAid                 int
   
)
proc: BEGIN
        
    
   update   t_mkt_material_info
   set     state = 0,
   type = null,
   show_name = null,
   path = null,
   update_time = now()
   where   aid = pAid;
end;