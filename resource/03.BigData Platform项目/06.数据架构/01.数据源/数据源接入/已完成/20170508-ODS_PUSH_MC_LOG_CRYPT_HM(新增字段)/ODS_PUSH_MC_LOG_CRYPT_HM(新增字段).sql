ODS_PUSH_MC_LOG_CRYPT_HM
ÐÂÔö×Ö¶Î£º
alter table ods_push_mc_log_crypt_hm add columns (deviceId string);  
alter table ods_push_mc_log_crypt_hm add columns (sendDate string);  
alter table ods_push_mc_log_crypt_hm add columns (msgType int); 
alter table ods_push_mc_log_crypt_hm add columns (taskeId string); 
alter table ods_push_mc_log_crypt_hm add columns (sendChannel int); 
alter table ods_push_mc_log_crypt_hm add columns (expireTime string); 
alter table ods_push_mc_log_crypt_hm add columns (userTraceId string); 