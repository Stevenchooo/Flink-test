#ҵ���Լ������Լ�������������������裬
# ��������CA����
#
#��װCA����
# 1��ֹͣԭ������
# 2��ɾ��ԭ������
# 2����ѹ���ļ�
# 3�������ļ�
# 4�������½���
#


TOMCAT_HOME=/root/apache-tomcat-7.0.57
WEBAPP_NAME=MktSystem
WEBAPP_WAR_NAME=MktSystem.war
SERVER_PORT=8080
CONF_PATH=/root/apache-tomcat-7.0.57/webapps/MktSystem/WEB-INF/conf

function modifySysConfig()
{
  cd $CONF_PATH
  echo `pwd`
  cp /root/MktSystem_bak_20150807/WEB-INF/conf/system.cfg   ./
}

function modifyDBConfig()
{
  return 0
}

function deployWebApps()
{
  cd $TOMCAT_HOME/webapps

  rm $WEBAPP_NAME -rf
  
  mkdir $WEBAPP_NAME
  
  cp $WEBAPP_WAR_NAME  $WEBAPP_NAME
  
  cd $WEBAPP_NAME
  
  jar -xvf $WEBAPP_WAR_NAME

  rm $WEBAPP_WAR_NAME
}

function shutdownTomcat()
{
   SERVER_PID=`lsof -i:$SERVER_PORT | grep LISTEN | awk '{print $2}'`
   echo "$IPADDR server is online, begin to shutdown"
   kill -9 $SERVER_PID
}

function startTomcat()
{
  cd $TOMCAT_HOME/bin
  sh startup.sh
}

shutdownTomcat
deployWebApps
modifySysConfig
startTomcat