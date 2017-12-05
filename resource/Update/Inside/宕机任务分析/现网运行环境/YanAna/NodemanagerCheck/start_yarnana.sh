#/bin/sh
#rm /home/bi/platform/NodemanagerCheck/files/*
#/home/bi/platform/NodemanagerCheck/autoScpGet.sh
./unzipFile.sh
rm -r /home/omm/xwx367844/bi_platform_team/NodemanagerCheck/database
java -jar  YarnAna.jar  /home/omm/xwx367844/bi_platform_team/NodemanagerCheck/files create_table=true delete_table=true
./connectdb.sh
