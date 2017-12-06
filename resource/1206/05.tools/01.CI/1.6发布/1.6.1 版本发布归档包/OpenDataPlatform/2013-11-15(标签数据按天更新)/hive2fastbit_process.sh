#!/bin/sh
#-----------------------------------------------------------------
# FileName: hive2fastbit_process.sh
# Author: yushuangxin 00220010
# Purpose: 导出hive文件到cnv格式，并生成fastbit索引文件
# Description: 
# Input:
# Output:
# History:
# Version:    Date         Author        Description
#-----------------------------------------------------------------
# 1.0         2013-11-12   y00220010     完成hive到fastbit索引生成脚本功能
# 创建导出数据和idx文件夹
cd $HOME/fastbitData
fdFolder="$date"
if [ ! -f $fdFolder ];then
    mkdir $fdFolder
fi

cd $HOME/fastbitIdx
idxFolder="$date"
if [ ! -f $idxFolder ];then
    mkdir $idxFolder
fi

# 导出环境变量，用于传递到hive脚本文件
export fdPath=$HOME"/fastbitData/"$fdFolder
sql_file="export_tsv_prod.sql"
exp_sql=$HOME"/HIBI_ExecuteShell/config/"$sql_file

export idxPath=$HOME"/fastbitIdx/"$idxFolder
export CURRENT_DATE="$date"
export CURRENT_DATE_EP="$date_ep"

iniFile=$HOME"/HIBI_ExecuteShell/config/hive2fastbit.ini"

# 从hive中导出文件
hive -i $iniFile -f $exp_sql

if test $? -ne 0;then
    echo "exit fail........"
    exit 1;
fi

# 转换特殊字符
echo REQUEST_KILL_PID:$$
echo "Operation starting..."

cd $HOME/HIBI_ExecuteShell/config
source tsv_cnv.sh

if test $? -ne 0;then
    echo "exit fail........"
    exit 1;
fi

# 生成fastbit数据文件
echo REQUEST_KILL_PID:$$
echo "Operation starting..."

source fastbit_data.sh

if test $? -ne 0;then
    echo "exit fail........"
    exit 1;
fi

# 生成fastbit索引文件
echo REQUEST_KILL_PID:$$
echo "Operation starting..."

source fastbit_idx.sh
