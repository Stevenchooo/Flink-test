mysql -h 10.71.3.199 -umktsystem -pRdmdlDVEQj76RLgffLNv mktsystemdb < /MFS/Share/hadoop-NJ/task/HiBI/HIBI/config/insert_table.sql
mysql -h 10.71.3.199 -umktsystem -pRdmdlDVEQj76RLgffLNv mktsystemdb  -N -e "select activtiy_id,activity_name,media_id,media_name,sid,begin_date,end_date,estimate_exp,estimate_click,media_type,url_host,landing_plate,resourceName,productName,cid,operator,state,channel,ad_position,material_type,dept_type,aid,delivery_times,port,platform from dim_mkt_base_info_ds " >/MFS/Share/hadoop-NJ/task/HiBI/HIBI/config/mkt/dim_mkt_base_info_ds.txt

if [ $? -ne 0 ];then
        echo 'export from hive fail'
        exit 1
else
                
                
                hadoop fs -rm 'hdfs://hacluster/hadoop-NJ/data/common/dim_mkt_base_info_ds/dim_mkt_base_info_ds.txt';
                hadoop fs -put '/MFS/Share/hadoop-NJ/task/HiBI/HIBI/config/mkt/dim_mkt_base_info_ds.txt' 'hdfs://hacluster/hadoop-NJ/data/common/dim_mkt_base_info_ds';
        
        echo 'import to hive success'
fi
