#!/usr/bin/python
import urllib2
import json
import sys
import time
addr="10.71.9.13:26014"
print "begin:", time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(time.time()))
f=open(sys.argv[1], "r")
fo=open("mapred.sql", "w")
try:  
  firstline=True
  sqls=[]
  while True:
    line=f.readline()
    if line:
      if firstline:
        firstline=False
        continue
  
      fields=line.split('\t')
      maps=0
      reduces=0
      jobnum=0
      taskid=fields[0]
      cycleid=fields[1]
      starttime=fields[2]
      endtime=fields[3]
      jobidfield=fields[4]
      redotimes=fields[5]
      rundate=fields[6]
      duration=fields[7]
      jobstart=0
      jobend=0
      jobduration=0
      longgest_jobid=""
      longgest_elapse=0
      biggest_map_jobid=""
      biggest_maps=0
      biggest_reduce_jobid=""
      biggest_reduces=0
      
      if jobidfield and (jobidfield!="NULL") and (jobidfield!=""):
        jobids=jobidfield.split(',')
        newjids=[]
        for jid in jobids:
          if jid not in newjids:
            newjids.append(jid)
        
        for jid in newjids:
          if len(jid and "job")<=0:
            continue
          try:
            jobnum += 1
            joburl="https://"+addr+"/ws/v1/history/mapreduce/jobs/" + jid
            #print joburl
            u = urllib2.urlopen(joburl)
            resp = json.loads(u.read().decode('utf-8'))
            maps += resp['job']['mapsTotal']
            reduces += resp['job']['reducesTotal']
            if (jobstart==0) or (resp['job']['startTime'] < jobstart):
              jobstart = resp['job']['startTime']
            if (resp['job']['finishTime'] > jobend):
              jobend=resp['job']['finishTime']
            if (resp['job']['finishTime']-resp['job']['startTime'] > longgest_elapse):
              longgest_elapse=resp['job']['finishTime']-resp['job']['startTime']
              longgest_jobid=jid            
            if (resp['job']['mapsTotal'] > biggest_maps):
              biggest_maps=resp['job']['mapsTotal']
              biggest_map_jobid=jid
            if (resp['job']['reducesTotal'] > biggest_reduces):
              biggest_reduces=resp['job']['reducesTotal']
              biggest_reduce_jobid=jid
          except Exception, e:
            print "error: taskid[", taskid, "] jobid[", jid, "]"
            print joburl
            continue 
      #print taskid,maps,reduces,jobnum
      jobduration=jobend-jobstart
      sql=("delete from tcc_mapred_info where task_id=%s and cycle_id='%s';\n" % (taskid,cycleid))
      sqls.append(sql)
      sql=("insert into tcc_mapred_info(task_id,cycle_id,task_start_time,task_end_time,task_duration,redo_times,job_start_time_first,job_end_time_last,job_duration,job_nums,maps_total,reduces_total,longest_jobid,longest_job_elapse,biggest_map_jobid,biggest_maps,biggest_reduce_jobid,biggest_reduces) values(%s,'%s','%s','%s',%s,%s,'%s','%s',%d,%d,%d,%d,'%s',%d,'%s',%d,'%s',%d);\n" % (taskid,cycleid,starttime,endtime,duration,redotimes,time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(jobstart/1000)),time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(jobend/1000)),jobduration/1000,jobnum,maps,reduces,longgest_jobid,longgest_elapse/1000,biggest_map_jobid,biggest_maps,biggest_reduce_jobid,biggest_reduces))
      sqls.append(sql)
    else:
      break
  
  fo.writelines(sqls)
except Exception , e:
  print e
finally:
  f.close()
  fo.close()
print "end:", time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(time.time()))