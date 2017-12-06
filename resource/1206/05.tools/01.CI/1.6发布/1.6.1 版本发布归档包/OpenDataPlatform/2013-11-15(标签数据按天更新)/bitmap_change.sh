v_classpath=$CLASSPATH
v_classpath="/home/hadoop/cloud/hbase-0.92.1/hbase-0.92.1.jar:$v_classpath"
v_classpath="/home/hadoop/cloud/hbase-0.92.1/lib/*:$v_classpath"
v_classpath="/home/hadoop/cloud/hadoop-1.0.1/lib/*:$v_classpath"
v_classpath="/home/hadoop/cloud/hive-0.9.0/lib/*:$v_classpath"
v_classpath="/data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/WEB-INF/lib/*:$v_classpath"
export CLASSPATH=$v_classpath
echo $CLASSPATH

v_lib_path1="/data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/WEB-INF/lib"
echo $v_lib_path1

export FASTBIT_HOME=/home/hadoop/cloud/fastbit-ibis1.3.6
v_lib_path2="$FASTBIT_HOME/src/.libs"
echo $v_lib_path2

echo "==============================================="
echo ""
export IBISRC=/data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/WEB-INF/classes/conf/ibis.rc
echo "IBISRC=$IBISRC"

echo ""
echo "==============================================="

echo ""
echo "old version $1"
echo "new version $2"
echo "==============================================="


#upgrade_schedules
LD_LIBRARY_PATH=$v_lib_path1:$v_lib_path2 java \
-Dlog4j.configuration=file:////data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/conf/log4j.xml \
-cp $CLASSPATH \
com.huawei.bt.schedule.FS_test upgrade_schedules $2  $1 \
2>>u_all.out 1>>u_all.out


#upgrade_scopes
LD_LIBRARY_PATH=$v_lib_path1:$v_lib_path2 java \
-Dlog4j.configuration=file:////data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/conf/log4j.xml \
-cp $CLASSPATH \
com.huawei.bt.schedule.FS_test upgrade_scopes $2  $1 \
2>>u_all.out 1>>u_all.out


#upgrade_usages
LD_LIBRARY_PATH=$v_lib_path1:$v_lib_path2 java \
-Dlog4j.configuration=file:////data2/bt_server/apache-tomcat-6.0.35_safe/webapps/bt_server_wwg/conf/log4j.xml \
-cp $CLASSPATH \
com.huawei.bt.schedule.FS_test upgrade_usages $2  $1 \
2>>u_all.out 1>>u_all.out

