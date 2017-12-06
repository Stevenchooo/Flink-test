#!/bin/bash
ROOT_PATH=`dirname $0`
JOB_PATH=$ROOT_PATH/DataLoad_0.1/DataLoad/DataLoad_run.sh

#解析参数
while getopts "a:p:" arg  
do  
    case $arg in  
    a)  
        #参数存在$OPTARG中  
        actionvalue=$OPTARG ;;  
    p)  
        ptvalue=$OPTARG ;;
    ?)  
        #不认识的arg为?  
        echo "unkonw argument";;  
    esac  
done 
echo "***************************************************************"
echo action=$actionvalue
echo pt=$ptvalue
echo "***************************************************************"

sh $JOB_PATH --context_param action=$actionvalue --context_param pt="$ptvalue"

sta=$?
if test $sta -ne 0;
then
        echo "exit fail......"
        exit $sta;
else
        echo "exit successfully......"
        exit 0;
fi

