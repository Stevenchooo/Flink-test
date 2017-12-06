#!/bash/bin

begin_time="20170430"
end_time=`date "+%Y%m%d"`
bin_path="/home/bdi/report_home/bin"
path=`pwd`


#####################################
#结算中间表
#####################################
while (( $begin_time <= $end_time ))
do
    end_time_tmp=`date -d "$begin_time 16 day" "+%Y%m%d"`
	
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_sdk_mid_current_report_d.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_sdk_mid_current_report_d.log
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_sdk_mid_current_report_dw.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_sdk_mid_current_report_dw.log
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_sdk_mid_current_report_dm.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_sdk_mid_current_report_dm.log
	
    begin_time=`date -d "$begin_time 15 day" "+%Y%m%d"`
done
echo "step 1 finshed" >>$path/run.log


#####################################
#结算用户活跃
#####################################
begin_time="20170430"

while (( $begin_time <= $end_time ))
do
    end_time_tmp=`date -d "$begin_time 16 day" "+%Y%m%d"`

    sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_active_report_d.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_active_report_d.log
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_active_report_dw.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_active_report_dw.log
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_active_report_dm.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_active_report_dm.log
	
    begin_time=`date -d "$begin_time 15 day" "+%Y%m%d"`
done
echo "step 2 finshed" >>$path/run.log

begin_time="20170430"
sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_active_report_w.xml -s ${begin_time}000000 -e ${end_time}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_active_report_w.log
sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_active_report_m.xml -s ${begin_time}000000 -e ${end_time}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_active_report_m.log
echo "step 3 finshed" >>$path/run.log

#####################################
#结算用户获取
#####################################
begin_time="20170430"

while (( $begin_time <= $end_time ))
do
    end_time_tmp=`date -d "$begin_time 16 day" "+%Y%m%d"`

    sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_grab_report_d.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_grab_report_d.log
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_grab_report_dw.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_grab_report_dw.log
	sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_grab_report_dm.xml -s ${begin_time}000000 -e ${end_time_tmp}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_grab_report_dm.log
	
    begin_time=`date -d "$begin_time 15 day" "+%Y%m%d"`
done
echo "step 4 finshed" >>$path/run.log

begin_time="20170430"
sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_grab_report_w.xml -s ${begin_time}000000 -e ${end_time}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_grab_report_w.log
sh ${bin_path}/start.sh -t /home/bdi/report_home/config/report/video_edp_user_grab_report_m.xml -s ${begin_time}000000 -e ${end_time}000000 -u video/hadoop -k video.keytab -q QueueC >>$path/video_edp_user_grab_report_m.log
echo "step 5 finshed" >>$path/run.log
