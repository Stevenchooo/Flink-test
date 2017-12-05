#/bin/sh
  cat scp.log | while read line
  do
    scp $line 10.51.30.55:/home/omm/xwx367844/bi_platform_team/ScpLog/data
  done
  
