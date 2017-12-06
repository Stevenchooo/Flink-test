# !/bin/sh
sleep 1
echo `lsof -i:8880`>>rebootlog
./shutdown.sh
echo `date` run tomcate...>>rebootlog
echo `lsof -i:8880`>>rebootlog
nohup ./catalina.sh run &

