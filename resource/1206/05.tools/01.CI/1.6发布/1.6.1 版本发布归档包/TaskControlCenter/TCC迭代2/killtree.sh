#!/bin/sh

if [ $# -ne 1 ]
then
    echo -e "./killtree PID">>killtree.log
    exit
else
    tpid=$1
    echo ./killtree $tpid>>killtree.log
fi

function killtree()
{
    local father=$1
    # stop father 
    echo -e "kill -STOP $father">>killtree.log
    kill -STOP $father 
    # children
    childs=(`ps -ef | awk -v father=$father 'BEGIN{ ORS=" "; } $3==father{ print $2; }'`)
    if [ ${#childs[@]} -ne 0 ]
    then
        for child in ${childs[*]}
        do
            killtree $child
        done
    fi

    # father 
    echo -e "kill -9 $father">>killtree.log
    kill -9 $father
}

killtree $tpid
