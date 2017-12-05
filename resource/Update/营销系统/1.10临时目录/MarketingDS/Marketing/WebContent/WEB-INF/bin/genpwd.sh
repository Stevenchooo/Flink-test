#!/bin/bash

cd `dirname $0`
cd ../

ROOT_DIR=`pwd`
LIB_DIR=${ROOT_DIR}/lib

CP=$CLASSPATH:$ROOT_DIR/classes

for j in $LIB_DIR/*.jar; do
    CP=$CP:$j
done

JAVA_OPTS="-Xms2048M -Xmx2048M -XX:+UseConcMarkSweepGC"
#JAVA_OPTS="$JAVA_OPTS -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider"
echo $CP

java $JAVA_OPTS -cp $CP com.huawei.util.EncryptUtil $1 $2 $3