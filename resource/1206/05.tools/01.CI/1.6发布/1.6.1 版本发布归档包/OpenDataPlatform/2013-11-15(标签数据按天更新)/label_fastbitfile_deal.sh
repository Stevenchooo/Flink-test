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

TAR_FILE_NAME="$PERIOD_TIME.tar.gz"

echo $TAR_FILE_NAME

cd $ROOT_PATH

FILE_DIR="$ROOT_PATH/$PERIOD_TIME"

VERSION="./index/v$PERIOD_TIME"

ln -s $FILE_DIR $VERSION

sta=$?

if test $sta -ne 0;
then
        echo "exit fail......"
        exit 1;
else
        echo "exit success......"
        exit 0;
fi
