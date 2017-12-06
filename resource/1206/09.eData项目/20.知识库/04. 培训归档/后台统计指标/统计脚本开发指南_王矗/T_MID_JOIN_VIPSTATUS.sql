drop table game.T_MID_JOIN_VIPSTATUS;
CREATE TABLE game.T_MID_JOIN_VIPSTATUS(                                   
    userid string,                                                         
    projectid string,                                                      
    timestamp string,                                                      
    chargetype string,                                                     
    chargeresult string,                                                   
    relchannel string,                                                     
    fee double,                                                            
    dtime string,                                                          
    unsubchannelid string,                                                 
    unsubtype string,                                                      
    activityid string,                                                     
    protectendtime string,                                                 
    promotioncpid string,                                                  
    productid string,                                                      
    affiliatechannelid string,                                             
    subscribetime string,
    chargechannelid string,
    currency string
)  
PARTITIONED BY (                                                           
    date string)                                                
ROW FORMAT delimited fields terminated by '\t' MAP KEYS TERMINATED BY '&' STORED AS TEXTFILE;

grant select,delete,insert on game.T_MID_JOIN_VIPSTATUS to user mapred;
grant select on table game.T_MID_JOIN_VIPSTATUS to user tianjunjie;


drop table game.T_MID_JOIN_SUBQUERY;
create table game.T_MID_JOIN_SUBQUERY
(
    projectid string,
    userid string,
    productid string,
    relchannel string,
    affiliatechannelid string,
    activityid string,
    dtime string,
    timestamp string,
    promotioncpid string,
    chargechannelid string,
    currency string
)
ROW FORMAT delimited fields terminated by '\t' MAP KEYS TERMINATED BY '&' STORED AS TEXTFILE;

grant select,delete,insert on game.T_MID_JOIN_SUBQUERY to user mapred;
grant select on table game.T_MID_JOIN_SUBQUERY to user tianjunjie;


drop table game.T_MID_JOIN_SUBQUERYTWO;
create table game.T_MID_JOIN_SUBQUERYTWO
(
    projectid string,
    userid string,
    relchannel string,
    affiliatechannelid string,
    activityid string,
    dtime string,
    promotioncpid string,
    chargechannelid string,
    currency string
)
ROW FORMAT delimited fields terminated by '\t' MAP KEYS TERMINATED BY '&' STORED AS TEXTFILE;

grant select,delete,insert on game.T_MID_JOIN_SUBQUERYTWO to user mapred;
grant select on table game.T_MID_JOIN_SUBQUERYTWO to user tianjunjie;

create table game.T_MID_BROWSER
(

    projectid          string ,
    relchannel         string ,
    affiliatechannelid string ,
    partnerid          string ,
    activityid         string ,
    dtime              string ,
    numacqrate_numerator      int,
    numacqrate_denominator    int,
    tpaysuccess        int
)
ROW FORMAT delimited fields terminated by '\t' MAP KEYS TERMINATED BY '&' STORED AS TEXTFILE;

grant select,delete,insert on game.T_MID_BROWSER to user mapred;
grant select on table game.T_MID_BROWSER to user tianjunjie;