#!/bin/sh
cd `dirname $0`
echo REQUEST_KILL_PID:$$
echo "Operation starting..."
source $HOME/.bashrc
JDK=$BIGDATA_HOME/JDK/jdk

$JDK/bin/java -jar HIBI-ExecuteShell.jar $@
sta=$?

if test $sta -ne 0;
then
	echo "exit fail......"
	exit 1;
else
	echo "exit successfully......"
	exit 0;
fi