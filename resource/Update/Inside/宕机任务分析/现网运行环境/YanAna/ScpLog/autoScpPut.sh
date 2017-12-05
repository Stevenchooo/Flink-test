#/bin/sh
# 删除老NodemanagerCheck下的老数据
rm /home/omm/xwx367844/bi_platform_team/NodemanagerCheck/files/*
 cd ./data

cp * /home/omm/xwx367844/bi_platform_team/NodemanagerCheck/files/
#chmod a+rw /home/bi/bi_platform_team/NodemanagerCheck/files/*

cp ../derby.sql /home/omm/xwx367844/bi_platform_team/NodemanagerCheck
#chmod a+r /home/bi/bi_platform_team/NodemanagerCheck/derby.sql
