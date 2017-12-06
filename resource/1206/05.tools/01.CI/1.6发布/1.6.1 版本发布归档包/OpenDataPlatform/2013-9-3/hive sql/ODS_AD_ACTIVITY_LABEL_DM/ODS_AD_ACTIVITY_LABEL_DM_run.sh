cd `dirname $0`
 ROOT_PATH=`pwd`
java -Xms256M -Xmx1024M -cp classpath.jar: bi_ods_2_2.ods_ad_activity_label_dm_0_1.ODS_AD_ACTIVITY_LABEL_DM --context_param filename=$1

if [ $? -eq 0 ];then
exit 0
else
exit 1
fi   