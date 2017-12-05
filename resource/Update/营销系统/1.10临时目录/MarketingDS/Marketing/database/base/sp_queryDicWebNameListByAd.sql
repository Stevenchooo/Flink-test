DROP PROCEDURE IF EXISTS `sp_queryDicWebNameListByAd`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_queryDicWebNameListByAd`(
     out pRetCode               int,
     in adName                  varchar(1024)

)
proc: BEGIN


  

  
  select activtiy_id,
       media_id     as id,
       media_name   as  name
  from 
       dim_mkt_base_info_ds where activity_name= adName
  group by activtiy_id,media_id,media_name;

  end//
DELIMITER ;