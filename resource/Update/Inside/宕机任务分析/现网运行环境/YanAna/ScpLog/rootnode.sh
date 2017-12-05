#/bin/sh
scp scplog.jar lf2-bi-fihadoop-71-41-5:~/
scp /home/omm/xwx367844/bi_platform_team/ScpLog/./subnode.sh lf2-bi-fihadoop-71-41-5:~/
ssh lf2-bi-fihadoop-71-41-5 <<eee
  cd ~
  java -classpath scplog.jar  com.scplog.IpNode /var/log/Bigdata/yarn/nm yarn-omm-nodemanager.*log.* 1490947800000 1490948700000 > scp.log
  chmod a+x subnode.sh
  ./subnode.sh
  rm ~/scplog.jar
  rm ~/scp.log
  rm ~/subnode.sh
eee
