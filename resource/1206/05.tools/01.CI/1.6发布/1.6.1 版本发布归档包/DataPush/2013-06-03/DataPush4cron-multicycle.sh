#! /bin/bash
#--parameters--
#周期类型[TYPE]：D天 H小时 M分钟
#时间跨度[SPAN]:传入格式为
#    TYPE为D时:20130425-20130501  //4月25号至5月1号内的数据，包含25但不包含1号，即左闭右开
#    TYPE为H时:2013042301-2013042523  //23号1时至25号22时内的数据，包含1时但不包含23时，即左闭右开
#    TYPE为M时:201304232235-201304252330  //23号22时35分开始，以步长累加，至左侧最接近25号23时30分的值为止，
#                                           即Min{TIME_END - n*STEP}且(TIME_END - n*STEP >= 0)
#步长值[STEP]:[1~60]的正整数，TYPE为M时有效,默认值为30
#--example--
#./DataPush4cron-multicycle.sh M 201304232200-201304232130 10
#./DataPush4cron-multicycle.sh H 2013042301-2013042523
#./DataPush4cron-multicycle.sh D 20130425-20130501

PERIOD_TYPE=$1
PERIOD_SPAN=$2
STEP=$3

PERIOD_LENGTH=30
# 当前时间的周期偏离量，负数表示向前偏移，正数表示向后偏移，单位取周期类型。
OFFSET=-1
ROOT_PATH=`dirname $0`
JOB_PATH=$ROOT_PATH/DataPush_0.1/DataPush/DataPush_run.sh

if [ X$PERIOD_TYPE == "X" ];then
    PERIOD_TYPE=D
fi

if [ X$PERIOD_SPAN == "X" ];then
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
    sh $JOB_PATH --context_param periodTime=$PERIOD_TIME --context_param periodType=$PERIOD_TYPE --context_param periodLength=$PERIOD_LENGTH

else
    
    PERIOD_TIME=""
    T1=`date +%Y%m%d`
    T2=`echo $PERIOD_SPAN |cut -d "-" -f 1|cut -c1-8`
    T3=`echo $PERIOD_SPAN |cut -d "-" -f 2| cut -c1-8`
    NOW=`date +%Y-%m-%d`
    TIME_START=`date +%Y-%m-%d -d $T2` &> /dev/null
    if [[ $? != 0 ]];then
        echo  TIME_START WRONG  PARAMS....
        echo exit ...
        exit 1
    fi
    TIME_END=`date +%Y-%m-%d -d $T3`  &>  /dev/null
    if [[ $? != 0 ]];then
        echo  TIME_END WRONG  PARAMS....
        echo exit ...
        exit 1
    fi

    HOUR=`date +%H`
    MINUTE=`date +%M`
    HOUR_START=`echo $PERIOD_SPAN |cut -d "-" -f 1|cut -c9-10`
    HOUR_END=`echo $PERIOD_SPAN |cut -d "-" -f 2|cut -c9-10`
    MIN_START=`echo $PERIOD_SPAN |cut -d "-" -f 1|cut -c11-12`
    MIN_END=`echo $PERIOD_SPAN |cut -d "-" -f 2|cut -c11-12`
    
    [ X$HOUR_START = "X" ] && THS=00 || THS=$HOUR_START;
    [ X$HOUR_END = "X" ] && THE=00 || THE=$HOUR_END;
    [ X$MIN_START = "X" ] && TMS=00|| TMS=$MIN_START;
    [ X$MIN_END = "X" ] && TME=00 || TME=$MIN_END;
    
    date -d "$TIME_START $THS:$TMS:00" &> /dev/null
    if [[ $? != 0 ]];then
        echo  TIME_START WRONG  PARAMS....
        echo exit ...
        exit 1
    fi
    date -d "$TIME_END $THE:$TME:00" &> /dev/null 
    if [[ $? != 0 ]];then
        echo TIME_END WRONG PARAMS....
        echo exit ...
        exit 1
    fi  
     
    if [ $PERIOD_TYPE = "D" ]; then

        if [[ $T2 > $T3 ]] || [[ $T3 > $T1 ]];
        then
            echo WRONG PARAMS....
            echo exit ...
            exit 1
        fi

        time1=$(($(($(date +%s --date="$NOW $HOUR:00:00") - $(date +%s --date="$TIME_START $HOUR:00:00")))/86400));
        time2=$(($(($(date +%s --date="$NOW $HOUR:00:00") - $(date +%s --date="$TIME_END $HOUR:00:00")))/86400));
        for (( OFFSET=time1;OFFSET>time2;OFFSET--))
        do
            PERIOD_TIME=`date +%Y%m%d --date="-$OFFSET day"`
            sh $JOB_PATH --context_param periodTime=$PERIOD_TIME --context_param periodType=$PERIOD_TYPE --context_param periodLength=$PERIOD_LENGTH
        done
 
    elif [ $PERIOD_TYPE = "H" ]; then

        time1=$(($(($(date +%s --date="$NOW $HOUR:00:00") - $(date +%s --date="$TIME_START $HOUR_START:00:00")))/3600));
        time2=$(($(($(date +%s --date="$NOW $HOUR:00:00") - $(date +%s --date="$TIME_END $HOUR_END:00:00")))/3600));
        
        if [[ $time1 < $time2 ]] || [[ $time2 < 0 ]];
        then
            echo WRONG PARAMS....
            echo exit ...
            exit 1
        fi
        
        for (( OFFSET=time1;OFFSET>time2;OFFSET--))
        do
            PERIOD_TIME=`date +%Y%m%d%H --date="-$OFFSET hour"`
            sh $JOB_PATH --context_param periodTime=$PERIOD_TIME --context_param periodType=$PERIOD_TYPE --context_param periodLength=$PERIOD_LENGTH
        done  
  
    elif [ $PERIOD_TYPE = "M" ];then
	    if [ X$STEP = "X" ];then
            STEP=30
        fi
		
		PERIOD_LENGTH=$STEP

        time1=$(($(($(date +%s --date="$NOW $HOUR:$MINUTE:00") - $(date +%s --date="$TIME_START $HOUR_START:$MIN_START:00")))/60));
        time2=$(($(($(date +%s --date="$NOW $HOUR:$MINUTE:00") - $(date +%s --date="$TIME_END $HOUR_END:$MIN_END:00")))/60));
        
        if [[ $time1 < $time2 ]] || [[ $time2 < 0 ]];
        then
            echo WRONG PARAMS....
            echo exit ...
            exit 1
        fi
  
        OFFSET=$time1
        while(true)
        do
        if [[ $OFFSET > $time2 ]];then
            if [ $(($OFFSET - $STEP)) -lt $time2 ];then
                break
            fi 
            PERIOD_TIME=`date +%Y%m%d%H%M --date="-$OFFSET minute"`
            sh $JOB_PATH --context_param periodTime=$PERIOD_TIME --context_param periodType=$PERIOD_TYPE --context_param periodLength=$PERIOD_LENGTH
            OFFSET=`expr $OFFSET - $STEP`
        else
            break
        fi
        done
    else
        echo "invalid PERIOD_TYPE: $PERIOD_TYPE"
    exit -1
    fi
fi



