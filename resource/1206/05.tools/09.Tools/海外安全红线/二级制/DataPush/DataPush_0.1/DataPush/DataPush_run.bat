%~d0
 cd %~dp0
 java -Xms256M -Xmx1024M -cp ../lib/systemRoutines.jar;../lib/userRoutines.jar;.;datapush_0_1.jar;sendfile_0_1.jar;filequality_0_1.jar;filedatapush_0_1.jar;multidatapush2_0_1.jar;scpfileext_0_1.jar;dbdatapush_0_1.jar;fileextract_0_1.jar;combinfile_0_1.jar;combinfileext_0_1.jar;scpfile_0_1.jar;dbextract_0_1.jar;../lib/commons-logging-1.1.1.jar;../lib/commons-logging-api-1.0.4.jar;../lib/dom4j-1.6.1.jar;../lib/filecopy.jar;../lib/ganymed-ssh2-261.jar;../lib/hadoop-core-0.20.203.0.jar;../lib/hibi-etl.jar;../lib/hive-exec-0.7.1.jar;../lib/hive-jdbc-0.7.1.jar;../lib/hive-metastore-0.7.1.jar;../lib/hive-service-0.7.1.jar;../lib/jakarta-oro-2.0.8.jar;../lib/jtds-1.2.5.jar;../lib/libfb303.jar;../lib/log4j-1.2.15.jar;../lib/mysql-connector-java-5.1.30-bin.jar;../lib/ojdbc6.jar;../lib/slf4j-api-1.6.1.jar;../lib/slf4j-log4j12-1.6.1.jar;../lib/talend-oracle-timestamptz.jar;../lib/talend_file_enhanced_20070724.jar;../lib/tns.jar; datapush.datapush_0_1.DataPush --context=Default %* 