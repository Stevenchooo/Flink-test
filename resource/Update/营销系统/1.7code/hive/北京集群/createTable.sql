 CREATE TABLE `dim_mkt_base_info_ds` (
 id             int(11) NOT NULL AUTO_INCREMENT ,
 activtiy_id    int(11) NOT NULL,        
 activity_name  varchar(256) NOT NULL,    
 media_id       int(11) NOT NULL,         
 media_name     varchar(256) NOT NULL,      
 sid            varchar(20),              
 begin_date     varchar(10),       
 end_date       varchar(10),         
 estimate_exp   int(11) DEFAULT '0',     
 estimate_click int(11) DEFAULT '0',   
 media_type     varchar(10) NOT NULL,       
 url_host       varchar(2048),         
 landing_plate  varchar(64),    
 resourceName   varchar(64),     
 productName    varchar(64),      
 cid            varchar(32),              
 operator       varchar(64),         
 state          int,            
 channel        varchar(512),
 PRIMARY KEY (`id`)          
 )                                                
 COLLATE='utf8_general_ci'                        
 ENGINE=InnoDB;

CREATE TABLE `dw_mkt_result_dm` 
(
  sid          VARCHAR(20),
  province     VARCHAR(128),
  city         VARCHAR(128),
  hour         VARCHAR(4),
  bg_pv       INT(11),
  bg_uv       INT(11),
  dj_pv       INT(11),
  dj_uv       INT(11),
  landing_pv  INT(11),
  landing_uv  INT(11),
  prom_users  INT(11),
  pay_users   INT(11),
  area_type   INT(1),
  user_type   INT(1),
  `pt_d`                 VARCHAR(8) DEFAULT NULL,
  `id`                   INT(11) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`id`,`pt_d`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;

CREATE TABLE `dw_exposure_frequency_dm` 
(
  activtiy_id             INT(11),
  media_id                INT(11),
  province                VARCHAR(128),
  frequency               INT(3),
  exp_frequency_users     INT(11),
  click_frequency_users   INT(11),
  landing_frequency_users INT(11),
  area_type               INT(1),
  user_type               INT(1),
  `pt_d`                 VARCHAR(8) DEFAULT NULL,
  `id`                   INT(11) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`id`,`pt_d`)

)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;


CREATE TABLE `dw_media_duplicate_dm`
(
  activtiy_id             INT(11),
  media_id                INT(11),
  secondary_media_id      INT(11),
  target_type             INT(1),
  duplicate_users         INT(11),
  area_type               INT(1),
  user_type               INT(1),
  `pt_d`                 VARCHAR(8) DEFAULT NULL,
  `id`                   INT(11) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`id`,`pt_d`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;



drop table dim_mkt_base_info_ds;
CREATE  TABLE IF NOT EXISTS dim_mkt_base_info_ds 
( 
activtiy_id     int,
activity_name   string,
media_id        int,
media_name      string,
sid             string,
begin_date      int,
end_date        int,
estimate_exp    int,
estimate_click  int,
media_type      int,
url_host        string,
landing_plate   string,
resourcename    string,
productname     string,
cid             string,
operator        string,
state           int,
channel         string,
ad_position     string
) 

CREATE EXTERNAL TABLE IF NOT EXISTS dim_mkt_base_info_ds 
( 
activtiy_id     int,
activity_name   string,
media_id        int,
media_name      string,
sid             string,
begin_date      int,
end_date        int,
estimate_exp    int,
estimate_click  int,
media_type      int,
url_host        string,
landing_plate   string,
resourcename    string,
productname     string,
cid             string,
operator        string,
state           int,
channel         string,
ad_position     string
) 
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\t' 
LINES TERMINATED BY '\n' 
LOCATION '/hadoop-NJ/data/common/dim_mkt_base_info_ds';