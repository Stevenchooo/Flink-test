#! /bin/bash
#modify it,user's path. the path of lzop,zip,java command
PATH=/usr/local/java/jdk/bin:/usr/local/bin:/usr/bin:/bin:$PATH
PERIOD_TYPE=D
PERIOD_LENGTH=30
OFFSET=-1
PERIOD_TIME=""

if [ $PERIOD_TYPE = "D" ]; then
    PERIOD_TIME=`date +%Y%m%d%H --date="$OFFSET day"`
elif [ $PERIOD_TYPE = "H" ]; then
    PERIOD_TIME=`date +%Y%m%d%H --date="$OFFSET hour"`
elif [ $PERIOD_TYPE = "M" ]; then
    MINUTE=`date +%M`
    if [ $MINUTE -gt 29 ];then
	   PERIOD_TIME=`date +%Y%m%d%H00`
	else
	   PERIOD_TIME=`date +%Y%m%d%H30 --date="-1 hour"`
	fi
else
    echo "invalid PERIOD_TYPE: $PERIOD_TYPE"
    exit -1
fi

# echo $PERIOD_TIME

ROOT_PATH=`dirname $0`
JOB_PATH=$ROOT_PATH/DataPush_0.1/DataPush/DataPush_run.sh

sh $JOB_PATH --context_param periodTime=$PERIOD_TIME --context_param periodType=$PERIOD_TYPE --context_param periodLength=$PERIOD_LENGTH

rtncode=$?
if [ $rtncode -ne 0 ];then
  echo "failed!";
  exit 1
else
  echo "successed!";
  exit 0
fi
