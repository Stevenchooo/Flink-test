#!/bin/sh
cd `dirname $0`
 ROOT_PATH=`pwd`
 CLASSPATH=`ls -1 $ROOT_PATH/../lib/*.jar | tr '\n' ':'`; export CLASSPATH
 CLASSPATH=`ls -1 $ROOT_PATH/*.jar | tr '\n' ':'`:$CLASSPATH; export CLASSPATH
 echo $CLASSPATH
 java -Xms256M -Xmx1024M bidataload.dataload_0_1.DataLoad --context=Default "$@" 