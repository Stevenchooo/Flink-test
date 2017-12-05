package org.apache.hadoop.mapreduce.tools;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.classification.InterfaceAudience.Public;
import org.apache.hadoop.classification.InterfaceStability.Stable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TIPStatus;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.JobPriority;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.JobStatus.State;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.TaskCompletionEvent;
import org.apache.hadoop.mapreduce.TaskReport;
import org.apache.hadoop.mapreduce.TaskTrackerInfo;
import org.apache.hadoop.mapreduce.TaskType;
import org.apache.hadoop.mapreduce.jobhistory.HistoryViewer;
import org.apache.hadoop.mapreduce.v2.LogParams;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.util.ExitUtil;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.yarn.logaggregation.LogCLIHelpers;
 
public class CLI2
  extends Configured
  implements Tool
{
  private static final Log LOG = LogFactory.getLog(CLI2.class);
  protected Cluster cluster;
  private static final Set<String> taskTypes = new HashSet(Arrays.asList(new String[] { "MAP", "REDUCE" }));
  private final Set<String> taskStates = new HashSet(Arrays.asList(new String[] { "running", "completed", "pending", "failed", "killed" }));
  
  public CLI2() {}
  
  public CLI2(Configuration conf)
  {
    setConf(conf);
  }
  
  public int run(String[] argv)
    throws Exception
  {
    int exitCode = -1;
    if (argv.length < 1)
    {
      displayUsage("");
      return exitCode;
    }
    String cmd = argv[0];
    String submitJobFile = null;
    String jobid = null;
    String taskid = null;
    String historyFile = null;
    String counterGroupName = null;
    String counterName = null;
    JobPriority jp = null;
    String taskType = null;
    String taskState = null;
    int fromEvent = 0;
    int nEvents = 0;
    boolean getStatus = false;
    boolean getCounter = false;
    boolean killJob = false;
    boolean listEvents = false;
    boolean viewHistory = false;
    boolean viewAllHistory = false;
    boolean listJobs = false;
    boolean listAllJobs = false;
    boolean listActiveTrackers = false;
    boolean listBlacklistedTrackers = false;
    boolean displayTasks = false;
    boolean killTask = false;
    boolean failTask = false;
    boolean setJobPriority = false;
    boolean logs = false;
    if ("-submit".equals(cmd))
    {
      if (argv.length != 2)
      {
        displayUsage(cmd);
        return exitCode;
      }
      submitJobFile = argv[1];
    }
    else if ("-status".equals(cmd))
    {
      if (argv.length != 2)
      {
        displayUsage(cmd);
        return exitCode;
      }
      jobid = argv[1];
      getStatus = true;
    }
    else if ("-counter".equals(cmd))
    {
      if (argv.length != 4)
      {
        displayUsage(cmd);
        return exitCode;
      }
      getCounter = true;
      jobid = argv[1];
      counterGroupName = argv[2];
      counterName = argv[3];
    }
    else if ("-kill".equals(cmd))
    {
      if (argv.length != 2)
      {
        displayUsage(cmd);
        return exitCode;
      }
      jobid = argv[1];
      killJob = true;
    }
    else if ("-set-priority".equals(cmd))
    {
      if (argv.length != 3)
      {
        displayUsage(cmd);
        return exitCode;
      }
      jobid = argv[1];
      try
      {
        jp = JobPriority.valueOf(argv[2]);
      }
      catch (IllegalArgumentException iae)
      {
        LOG.info(iae);
        displayUsage(cmd);
        return exitCode;
      }
      setJobPriority = true;
    }
    else if ("-events".equals(cmd))
    {
      if (argv.length != 4)
      {
        displayUsage(cmd);
        return exitCode;
      }
      jobid = argv[1];
      fromEvent = Integer.parseInt(argv[2]);
      nEvents = Integer.parseInt(argv[3]);
      listEvents = true;
    }
    else if ("-history".equals(cmd))
    {
      if ((argv.length != 2) && ((argv.length != 3) || (!"all".equals(argv[1]))))
      {
        displayUsage(cmd);
        return exitCode;
      }
      viewHistory = true;
      if ((argv.length == 3) && ("all".equals(argv[1])))
      {
        viewAllHistory = true;
        historyFile = argv[2];
      }
      else
      {
        historyFile = argv[1];
      }
    }
    else if ("-list".equals(cmd))
    {
      if ((argv.length != 1) && ((argv.length != 2) || (!"all".equals(argv[1]))))
      {
        displayUsage(cmd);
        return exitCode;
      }
      if ((argv.length == 2) && ("all".equals(argv[1]))) {
        listAllJobs = true;
      } else {
        listJobs = true;
      }
    }
    else if ("-kill-task".equals(cmd))
    {
      if (argv.length != 2)
      {
        displayUsage(cmd);
        return exitCode;
      }
      killTask = true;
      taskid = argv[1];
    }
    else if ("-fail-task".equals(cmd))
    {
      if (argv.length != 2)
      {
        displayUsage(cmd);
        return exitCode;
      }
      failTask = true;
      taskid = argv[1];
    }
    else if ("-list-active-trackers".equals(cmd))
    {
      if (argv.length != 1)
      {
        displayUsage(cmd);
        return exitCode;
      }
      listActiveTrackers = true;
    }
    else if ("-list-blacklisted-trackers".equals(cmd))
    {
      if (argv.length != 1)
      {
        displayUsage(cmd);
        return exitCode;
      }
      listBlacklistedTrackers = true;
    }
    else if ("-list-attempt-ids".equals(cmd))
    {
      if (argv.length != 4)
      {
        displayUsage(cmd);
        return exitCode;
      }
      jobid = argv[1];
      taskType = argv[2];
      taskState = argv[3];
      displayTasks = true;
      if (!taskTypes.contains(org.apache.hadoop.util.StringUtils.toUpperCase(taskType)))
      {
        System.out.println("Error: Invalid task-type: " + taskType);
        displayUsage(cmd);
        return exitCode;
      }
      if (!this.taskStates.contains(org.apache.hadoop.util.StringUtils.toLowerCase(taskState)))
      {
        System.out.println("Error: Invalid task-state: " + taskState);
        displayUsage(cmd);
        return exitCode;
      }
    }
    else if ("-logs".equals(cmd))
    {
      if ((argv.length == 2) || (argv.length == 3))
      {
        logs = true;
        jobid = argv[1];
        if (argv.length == 3) {
          taskid = argv[2];
        } else {
          taskid = null;
        }
      }
      else
      {
        displayUsage(cmd);
        return exitCode;
      }
    }
    else
    {
      displayUsage(cmd);
      return exitCode;
    }
    this.cluster = createCluster();
    try
    {
      if (submitJobFile != null)
      {
        Job job = Job.getInstance(new JobConf(submitJobFile));
        job.submit();
        System.out.println("Created job " + job.getJobID());
        exitCode = 0;
      }
      else if (getStatus)
      {
    	  String resultString= jobid ;
    	  String SPLIT_FLAG="\t";
        Job job = this.cluster.getJob(JobID.forName(jobid));
        if (job == null)
        {
          //System.out.println("Could not find job " + jobid);
        }
        else
        {
          Counters counters = job.getCounters();
          //System.out.println();
          //System.out.println(job);
          resultString+=SPLIT_FLAG +job.getStatus().getState();
          if (counters != null) {
           // System.out.println(counters);
        CounterGroup cg = counters.getGroup("FileSystemCounters");
        	String[] countFlag={"FILE_BYTES_READ",     
        			"FILE_BYTES_WRITTEN",   
        			"FILE_READ_OPS",        
        			"FILE_LARGE_READ_OPS",  
        			"FILE_WRITE_OPS",       
        			"HDFS_BYTES_READ",      
        			"HDFS_BYTES_WRITTEN",   
        			"HDFS_READ_OPS",        
        			"HDFS_LARGE_READ_OPS",  
        			"HDFS_WRITE_OPS"};
        	//Iterator תmap
        	HashMap map = new HashMap<String, String>();
            for (Counter c:cg ) {
            	map.put(c.getName(), c.getValue());
            }
            
            for(String f: countFlag)
            {
            	resultString += SPLIT_FLAG + map.get(f);
            	
            }
          } else {
            //System.out.println("Counters not available. Job is retired.");
          }
          exitCode = 0;
        }
        System.out.println(resultString);
      }
      else if (getCounter)
      {
        Job job = this.cluster.getJob(JobID.forName(jobid));
        if (job == null)
        {
          System.out.println("Could not find job " + jobid);
        }
        else
        {
          Counters counters = job.getCounters();
          if (counters == null)
          {
            System.out.println("Counters not available for retired job " + jobid);
            
            exitCode = -1;
          }
          else
          {
        	  CounterGroup cg = counters.getGroup(counterGroupName);
              for (Counter c:cg ) {
              	System.out.println("=="+c.getName() +"= " + c.getValue());
                }
            
            exitCode = 0;
          }
        }
      }
      else if (killJob)
      {
        Job job = this.cluster.getJob(JobID.forName(jobid));
        if (job == null)
        {
          System.out.println("Could not find job " + jobid);
        }
        else
        {
          JobStatus jobStatus = job.getStatus();
          if (jobStatus.getState() == JobStatus.State.FAILED)
          {
            //System.out.println("Could not mark the job " + jobid + " as killed, as it has already failed.");
            
            exitCode = -1;
          }
          else if (jobStatus.getState() == JobStatus.State.KILLED)
          {
            //System.out.println("The job " + jobid + " has already been killed.");
            
            exitCode = -1;
          }
          else if (jobStatus.getState() == JobStatus.State.SUCCEEDED)
          {
            //System.out.println("Could not kill the job " + jobid + ", as it has already succeeded.");
            
            exitCode = -1;
          }
          else
          {
            job.killJob();
            System.out.println("Killed job " + jobid);
            exitCode = 0;
          }
        }
      }
      else if (setJobPriority)
      {
        Job job = this.cluster.getJob(JobID.forName(jobid));
        if (job == null)
        {
          System.out.println("Could not find job " + jobid);
        }
        else
        {
          job.setPriority(jp);
          
          System.out.println("Not implemented yet.");
          exitCode = 0;
        }
      }
      else if (viewHistory)
      {
        viewHistory(historyFile, viewAllHistory);
        exitCode = 0;
      }
      else if (listEvents)
      {
        listEvents(this.cluster.getJob(JobID.forName(jobid)), fromEvent, nEvents);
        exitCode = 0;
      }
      else if (listJobs)
      {
        listJobs(this.cluster);
        exitCode = 0;
      }
      else if (listAllJobs)
      {
        listAllJobs(this.cluster);
        exitCode = 0;
      }
      else if (listActiveTrackers)
      {
        listActiveTrackers(this.cluster);
        exitCode = 0;
      }
      else if (listBlacklistedTrackers)
      {
        listBlacklistedTrackers(this.cluster);
        exitCode = 0;
      }
      else if (displayTasks)
      {
        displayTasks(this.cluster.getJob(JobID.forName(jobid)), taskType, taskState);
        exitCode = 0;
      }
      else if (killTask)
      {
        TaskAttemptID taskID = TaskAttemptID.forName(taskid);
        Job job = this.cluster.getJob(taskID.getJobID());
        if (job == null)
        {
          System.out.println("Could not find job " + jobid);
        }
        else if (job.killTask(taskID, false))
        {
          System.out.println("Killed task " + taskid);
          exitCode = 0;
        }
        else
        {
          System.out.println("Could not kill task " + taskid);
          exitCode = -1;
        }
      }
      else if (failTask)
      {
        TaskAttemptID taskID = TaskAttemptID.forName(taskid);
        Job job = this.cluster.getJob(taskID.getJobID());
        if (job == null)
        {
          System.out.println("Could not find job " + jobid);
        }
        else if (job.killTask(taskID, true))
        {
          System.out.println("Killed task " + taskID + " by failing it");
          exitCode = 0;
        }
        else
        {
          System.out.println("Could not fail task " + taskid);
          exitCode = -1;
        }
      }
      else if (logs)
      {
        try
        {
          JobID jobID = JobID.forName(jobid);
          TaskAttemptID taskAttemptID = TaskAttemptID.forName(taskid);
          LogParams logParams = this.cluster.getLogParams(jobID, taskAttemptID);
          LogCLIHelpers logDumper = new LogCLIHelpers();
          logDumper.setConf(getConf());
          exitCode = logDumper.dumpAContainersLogs(logParams.getApplicationId(), logParams.getContainerId(), logParams.getNodeId(), logParams.getOwner());
        }
        catch (IOException e)
        {
          if ((e instanceof RemoteException)) {
            throw e;
          }
          System.out.println(e.getMessage());
        }
      }
    }
    catch (RemoteException re)
    {
      IOException unwrappedException = re.unwrapRemoteException();
      if ((unwrappedException instanceof AccessControlException)) {
        System.out.println(unwrappedException.getMessage());
      } else {
        throw re;
      }
    }
    finally
    {
      this.cluster.close();
    }
    return exitCode;
  }
  
  Cluster createCluster()
    throws IOException
  {
    return new Cluster(getConf());
  }
  
  private String getJobPriorityNames()
  {
    StringBuffer sb = new StringBuffer();
    for (JobPriority p : JobPriority.values()) {
      sb.append(p.name()).append(" ");
    }
    return sb.substring(0, sb.length() - 1);
  }
  
  private String getTaskTypes()
  {
    return org.apache.commons.lang.StringUtils.join(taskTypes, " ");
  }
  
  private void displayUsage(String cmd)
  {
    String prefix = "Usage: job ";
    String jobPriorityValues = getJobPriorityNames();
    String taskStates = "running, completed";
    if ("-submit".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <job-file>]");
    }
    else if (("-status".equals(cmd)) || ("-kill".equals(cmd)))
    {
      System.err.println(prefix + "[" + cmd + " <job-id>]");
    }
    else if ("-counter".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <job-id> <group-name> <counter-name>]");
    }
    else if ("-events".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <job-id> <from-event-#> <#-of-events>]. Event #s start from 1.");
    }
    else if ("-history".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <jobHistoryFile>]");
    }
    else if ("-list".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " [all]]");
    }
    else if (("-kill-task".equals(cmd)) || ("-fail-task".equals(cmd)))
    {
      System.err.println(prefix + "[" + cmd + " <task-attempt-id>]");
    }
    else if ("-set-priority".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <job-id> <priority>]. " + "Valid values for priorities are: " + jobPriorityValues);
    }
    else if ("-list-active-trackers".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + "]");
    }
    else if ("-list-blacklisted-trackers".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + "]");
    }
    else if ("-list-attempt-ids".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <job-id> <task-type> <task-state>]. " + "Valid values for <task-type> are " + getTaskTypes() + ". " + "Valid values for <task-state> are " + taskStates);
    }
    else if ("-logs".equals(cmd))
    {
      System.err.println(prefix + "[" + cmd + " <job-id> <task-attempt-id>]. " + " <task-attempt-id> is optional to get task attempt logs.");
    }
    else
    {
      System.err.printf(prefix + "<command> <args>%n", new Object[0]);
      System.err.printf("\t[-submit <job-file>]%n", new Object[0]);
      System.err.printf("\t[-status <job-id>]%n", new Object[0]);
      System.err.printf("\t[-counter <job-id> <group-name> <counter-name>]%n", new Object[0]);
      System.err.printf("\t[-kill <job-id>]%n", new Object[0]);
      System.err.printf("\t[-set-priority <job-id> <priority>]. Valid values for priorities are: " + jobPriorityValues + "%n", new Object[0]);
      
      System.err.printf("\t[-events <job-id> <from-event-#> <#-of-events>]%n", new Object[0]);
      System.err.printf("\t[-history <jobHistoryFile>]%n", new Object[0]);
      System.err.printf("\t[-list [all]]%n", new Object[0]);
      System.err.printf("\t[-list-active-trackers]%n", new Object[0]);
      System.err.printf("\t[-list-blacklisted-trackers]%n", new Object[0]);
      System.err.println("\t[-list-attempt-ids <job-id> <task-type> <task-state>]. Valid values for <task-type> are " + getTaskTypes() + ". " + "Valid values for <task-state> are " + taskStates);
      


      System.err.printf("\t[-kill-task <task-attempt-id>]%n", new Object[0]);
      System.err.printf("\t[-fail-task <task-attempt-id>]%n", new Object[0]);
      System.err.printf("\t[-logs <job-id> <task-attempt-id>]%n%n", new Object[0]);
      ToolRunner.printGenericCommandUsage(System.out);
    }
  }
  
  private void viewHistory(String historyFile, boolean all)
    throws IOException
  {
    HistoryViewer historyViewer = new HistoryViewer(historyFile, getConf(), all);
    
    historyViewer.print();
  }
  
  protected long getCounter(Counters counters, String counterGroupName, String counterName)
    throws IOException
  {
    return counters.findCounter(counterGroupName, counterName).getValue();
  }
  
  private void listEvents(Job job, int fromEventId, int numEvents)
    throws IOException, InterruptedException
  {
    TaskCompletionEvent[] events = job.getTaskCompletionEvents(fromEventId, numEvents);
    
    System.out.println("Task completion events for " + job.getJobID());
    System.out.println("Number of events (from " + fromEventId + ") are: " + events.length);
    for (TaskCompletionEvent event : events) {
      System.out.println(event.getStatus() + " " + event.getTaskAttemptId() + " " + getTaskLogURL(event.getTaskAttemptId(), event.getTaskTrackerHttp()));
    }
  }
  
  protected static String getTaskLogURL(TaskAttemptID taskId, String baseUrl)
  {
    return baseUrl + "/tasklog?plaintext=true&attemptid=" + taskId;
  }
  
  private void listJobs(Cluster cluster)
    throws IOException, InterruptedException
  {
    List<JobStatus> runningJobs = new ArrayList();
    for (JobStatus job : cluster.getAllJobStatuses()) {
      if (!job.isJobComplete()) {
        runningJobs.add(job);
      }
    }
    displayJobList((JobStatus[])runningJobs.toArray(new JobStatus[0]));
  }
  
  private void listAllJobs(Cluster cluster)
    throws IOException, InterruptedException
  {
    displayJobList(cluster.getAllJobStatuses());
  }
  
  private void listActiveTrackers(Cluster cluster)
    throws IOException, InterruptedException
  {
    TaskTrackerInfo[] trackers = cluster.getActiveTaskTrackers();
    for (TaskTrackerInfo tracker : trackers) {
      System.out.println(tracker.getTaskTrackerName());
    }
  }
  
  private void listBlacklistedTrackers(Cluster cluster)
    throws IOException, InterruptedException
  {
    TaskTrackerInfo[] trackers = cluster.getBlackListedTaskTrackers();
    if (trackers.length > 0) {
      System.out.println("BlackListedNode \t Reason");
    }
    for (TaskTrackerInfo tracker : trackers) {
      System.out.println(tracker.getTaskTrackerName() + "\t" + tracker.getReasonForBlacklist());
    }
  }
  
  private void printTaskAttempts(TaskReport report)
  {
    if (report.getCurrentStatus() == TIPStatus.COMPLETE) {
      System.out.println(report.getSuccessfulTaskAttemptId());
    } else if (report.getCurrentStatus() == TIPStatus.RUNNING) {
      for (TaskAttemptID t : report.getRunningTaskAttemptIds()) {
        System.out.println(t);
      }
    }
  }
  
  protected void displayTasks(Job job, String type, String state)
    throws IOException, InterruptedException
  {
    TaskReport[] reports = job.getTaskReports(TaskType.valueOf(org.apache.hadoop.util.StringUtils.toUpperCase(type)));
    for (TaskReport report : reports)
    {
      TIPStatus status = report.getCurrentStatus();
      if (((state.equalsIgnoreCase("pending")) && (status == TIPStatus.PENDING)) || ((state.equalsIgnoreCase("running")) && (status == TIPStatus.RUNNING)) || ((state.equalsIgnoreCase("completed")) && (status == TIPStatus.COMPLETE)) || ((state.equalsIgnoreCase("failed")) && (status == TIPStatus.FAILED)) || ((state.equalsIgnoreCase("killed")) && (status == TIPStatus.KILLED))) {
        printTaskAttempts(report);
      }
    }
  }
  
  public void displayJobList(JobStatus[] jobs)
    throws IOException, InterruptedException
  {
    displayJobList(jobs, new PrintWriter(new OutputStreamWriter(System.out, Charsets.UTF_8)));
  }
  
 
  @InterfaceAudience.Private
  public static String headerPattern = "%23s\t%10s\t%14s\t%12s\t%12s\t%10s\t%15s\t%15s\t%8s\t%8s\t%10s\t%10s\n";
 
 
  @InterfaceAudience.Private
  public static String dataPattern = "%23s\t%10s\t%14d\t%12s\t%12s\t%10s\t%15s\t%15s\t%8s\t%8s\t%10s\t%10s\n";
  private static String memPattern = "%dM";
  private static String UNAVAILABLE = "N/A";
   
  public void displayJobList(JobStatus[] jobs, PrintWriter writer)
  {
    writer.println("Total jobs:" + jobs.length);
    writer.printf(headerPattern, new Object[] { "JobId", "State", "StartTime", "UserName", "Queue", "Priority", "UsedContainers", "RsvdContainers", "UsedMem", "RsvdMem", "NeededMem", "AM info" });
    for (JobStatus job : jobs)
    {
      int numUsedSlots = job.getNumUsedSlots();
      int numReservedSlots = job.getNumReservedSlots();
      int usedMem = job.getUsedMem();
      int rsvdMem = job.getReservedMem();
      int neededMem = job.getNeededMem();
       
       
      writer.printf(dataPattern, new Object[] { job.getJobID().toString(), job.getState(), Long.valueOf(job.getStartTime()), job.getUsername(), job.getQueue(), job.getPriority().name(), numUsedSlots < 0 ? UNAVAILABLE : Integer.valueOf(numUsedSlots), numReservedSlots < 0 ? UNAVAILABLE : Integer.valueOf(numReservedSlots), usedMem < 0 ? UNAVAILABLE : String.format(memPattern, new Object[] { Integer.valueOf(usedMem) }), rsvdMem < 0 ? UNAVAILABLE : String.format(memPattern, new Object[] { Integer.valueOf(rsvdMem) }), neededMem < 0 ? UNAVAILABLE : String.format(memPattern, new Object[] { Integer.valueOf(neededMem) }), job.getSchedulingInfo() });
       
    }
    writer.flush();
  }
  
  public static void main(String[] argv)
    throws Exception
  {
    int res = ToolRunner.run(new CLI2(), argv);
    ExitUtil.terminate(res);
  }
}
