#! /bin/sh
echo $1
date=${1:0:8 }
#推送点击数据（dwr_ad_click）开始
#判断文件夹是否存在
myfolder="/MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/$date"
if [ ! -d "$myfolder" ]; then
    mkdir "$myfolder"
    echo "mkdir $myfolder successfully"
else
    echo "$myfolder is exit"
fi
#从ftp服务器将数据scp到数据通道
#scp -r TCSM@10.41.24.13:/opt/huawei/ftp/theme/opt/huawei/ftp/theme/20160425/dwr_ad_click /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/20160425
#mkdir /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/$date
function scpfile(){
scp -r TCSM@10.71.7.29:/opt/huawei/ftp/theme/opt/huawei/ftp/theme/dwr_ad_click_$date*.zip /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/$date

if [ $? -eq 0 ]; then
    echo "scp $date dwr_ad_click successfully......"
else 
    echo "scp $date dwr_ad_click failed......"
    sleep 600
    scpfile;
fi
}
scpfile;
#解压
filelist=`ls /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/$date/dwr_ad_click_*.zip`
for file in $filelist
do 
 echo $file
 unzip -o -P AdMHW_View_Click_Tagp $file -d /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/$date/
done
if [ $? -eq 0 ]; then
    echo "unzip $date dwr_ad_click successfully......"
else 
    echo "unzip $date dwr_ad_click failed......"
    exit 1
fi

#改名
filelist=`ls /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/$date/dwr_ad_click_*.txt`
for file in $filelist
do 
 echo $file
 echo ${file##*/}
 cp $file /MFS/DataIn/hadoop-NJ/odsdata/ODS_COOPERATION_DWR_AD_CLICK_DM/t_$date"000000"${file##*/}.tsv
done

if [ $? -eq 0 ]; then
    echo "rename $date dwr_ad_click successfully......"
else 
    echo "rename $date dwr_ad_click failed......"
    exit 1
fi
