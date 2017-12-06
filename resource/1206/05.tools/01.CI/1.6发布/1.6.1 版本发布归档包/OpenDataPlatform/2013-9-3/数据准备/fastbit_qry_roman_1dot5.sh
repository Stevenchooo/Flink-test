echo "" > fb_qry4x.out
$FASTBIT_HOME/examples/ibis \
-v 2 \
-q 'select rowkey, app_channel where app_channel contains ("com.huawei.android.pushagent","com.huawei.appmarket")' \
-d "/data1/BIDev/Persona/Persona_dev/_src/bi_hive/_run/tsv_ubs/tsv_index_alliance1dot5" \
>>fb_qry4x.out 2>>fb_qry4x.out

#-q "select rowkey, app_list where app_list like '%aaa%' " \
#-q 'select rowkey, app_list ' \
#-q 'select rowkey, app_list where rowkey in ("aaa", "bbb") '
#-q 'select rowkey, app_list where app_list IN ("yyy", "xxx;yyy")'
#-q 'select rowkey, app_list where yyy in app_list
#-q 'select rowkey, app_list where app_list IN ("xxx;yyy", "xxx", "yyy")'
#\

tail -30 fb_qry4x.out
