#/bin/sh
 rm /home/omm/xwx367844/bi_platform_team/ScpLog/data/*
java -classpath scplog.jar com.scplog.RootNode
./autoScpPut.sh
rm /home/omm/xwx367844/bi_platform_team/ScpLog/data/*

