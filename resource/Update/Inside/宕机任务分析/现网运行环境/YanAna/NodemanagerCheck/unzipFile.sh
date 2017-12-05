#/bin/sh
cd /home/omm/xwx367844/bi_platform_team/NodemanagerCheck/files
ls -l *zip |awk -F ' ' '{print $9}' > a.txt
cat a.txt | while read line
do 
  unzip  $line
  rm $line
done
rm a.txt
#mv derby.sql ../derby.sql
