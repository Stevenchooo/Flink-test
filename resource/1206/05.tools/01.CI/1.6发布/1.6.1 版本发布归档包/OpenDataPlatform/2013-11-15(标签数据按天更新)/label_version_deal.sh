#!/bin/sh
PERIOD_TYPE=D
OFFSET=-1

PERIOD_TIME=""

if [ $PERIOD_TYPE = "D" ]; then
    PERIOD_TIME=${1:0:8}
fi

echo $PERIOD_TIME


ROOT_PATH=`dirname $0`

echo $ROOT_PATH

NEW_VERSION_NAME="v$PERIOD_TIME"

echo $NEW_VERSION_NAME

cd $ROOT_PATH

tmpInfo=`cat fast_scheduler.properties | grep "^index.latest_version"`

echo $tmpInfo

OLD_VERSION_NAME=${tmpInfo/'index.latest_version='/''}

echo $OLD_VERSION_NAME

TOMCAT_BIN_PATH="/data2/bt_server/apache-tomcat-6.0.35_safe/bin"

echo $TOMCAT_BIN_PATH

#kill tomcat
cd $TOMCAT_BIN_PATH

./shutdown.sh

wait

#edit version
cd $ROOT_PATH

OLD_LINE="index.latest_version=$OLD_VERSION_NAME"

echo $OLD_LINE

NEW_LINE="index.latest_version=$NEW_VERSION_NAME"

echo $NEW_LINE


sed "/$OLD_LINE/c\\$NEW_LINE" fast_scheduler.properties > fast_scheduler.properties_tmp

mv fast_scheduler.properties fast_scheduler.properties_bak_$PERIOD_TIME

mv fast_scheduler.properties_tmp fast_scheduler.properties

dos2unix fast_scheduler.properties

#start tomcat

cd $TOMCAT_BIN_PATH
./startup.sh

wait
#bitmap change
echo "bitmap change!"

BIT_MAP_SHELLFILE_DIR="/data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/WEB-INF/classes/_fastscheduler_run"

echo $BIT_MAP_SHELLFILE_DIR

cd $BIT_MAP_SHELLFILE_DIR

echo "begin time!" `date`

./bitmap_change.sh $OLD_VERSION_NAME $NEW_VERSION_NAME

echo "end time!" `date`
sta=$?

if test $sta -ne 0;
then
        echo "exit fail......"
        exit 1;
else
        echo "exit success......"
        exit 0;
fi
