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

FILE_NAME=$PERIOD_TIME

echo $FILE_NAME

TAR_FILE_NAME="$PERIOD_TIME.tar.gz"

echo $TAR_FILE_NAME

echo "tar -czvf $TAR_FILE_NAME  $FILE_NAME"

cd $ROOT_PATH

fileName=$FILE_NAME

userName=hadoop

ipAddress=10.120.5.79

destDir=/data1/wjl_fastbit_index

userPassWd=hadoop

./scpFile.exp "$fileName" "$userName" "$ipAddress" "$destDir" "$userPassWd"

wait

sta=$?

if test $sta -ne 0;
then
        echo "exit fail......"
        exit 1;
else
        echo "exit success...."
        exit 0;
fi
