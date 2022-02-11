package test;
import java.util.*;

public class JobsRequest extends Thread {  //作业请求类
	private static Clock clock;
	private int lastCheckTime;   //上次作业请求检查时间
	private static List<Job> JobsQueue = new LinkedList<Job>();   //作业队列
	private static int sumJob = 0;  //作业的总数
	
	public JobsRequest() {
		clock = new Clock();
		lastCheckTime = -1;
	}
	public static void jobInQueue(Job j) {  //作业入队
		JobsQueue.add(j);
		addSumJob();
	}
	public static Job jobOutQueue() {  //队头出队
		return JobsQueue.remove(0);
	}
	public static Job getJobHead() {  //获得队头作业
		return JobsQueue.get(0);
	}
	public static int getJobsLength() {
		return JobsQueue.size();
	}
	public static int getSumJob() {
		return sumJob;
	}
	public static void addSumJob() {
		sumJob++;
	}
	public static Job createANewJob(int instrucNum) {  //创建一个新作业
		Job job = new Job(getSumJob()+1, clock.getTime(), instrucNum);
		for(int i=0;i<instrucNum;i++) {
			Instruction ins = new Instruction(i+1,new Random().nextInt(4));
			job.addJobInstruction(ins);
		}
		return job;
	}
	public static void addJob(Job j) {  //将创建的Job加入队列，并根据Job进入时间进行排序
		for(int i=0;i<JobsQueue.size();i++) {
			if(j.getInTimes()<JobsQueue.get(i).getInTimes()) {
				JobsQueue.add(i, j);
				addSumJob();
				return;
			}
		}
	}
	public void run() {
		while(true) {
			if(clock.getTime()%5==0 && clock.getTime()!=lastCheckTime) {
				lastCheckTime = clock.getTime();
				//作业检查并创建进程进入就绪队列
				int i=0;
				while(JobsQueue.get(0)!=null && clock.getTime()>=JobsQueue.get(0).getInTimes()) {  
					Job j = JobsQueue.remove(0);
					System.out.println(clock.getTime()+":[新增作业"+j.getJobsID()+"]");
					IOFile.writeMessageInData(clock.getTime()+":[新增作业"+j.getJobsID()+"]");
					ProcessUI.addMessage(clock.getTime()+":[新增作业"+j.getJobsID()+"]");
					
					ProcessSchedule.addReadyQ(ProcessSchedule.createProcess(j));
					i++;
					//调整同一时间点进入的作业的优先级执行先后顺序
					if(i>=2) {
						for(int m=0;m<i-1;m++) {
							if(ProcessSchedule.ReadyQ.get(ProcessSchedule.ReadyQ.size()-m-1).getPcb().getPriority()<
								ProcessSchedule.ReadyQ.get(ProcessSchedule.ReadyQ.size()-m-2).getPcb().getPriority()) {
								Collections.swap(ProcessSchedule.ReadyQ, 
										ProcessSchedule.ReadyQ.size()-m-1, 
										ProcessSchedule.ReadyQ.size()-m-2);
								int temp = ProcessSchedule.ReadyQ.get(ProcessSchedule.ReadyQ.size()-m-1).getPcb().getRqNum();
								ProcessSchedule.ReadyQ.get(ProcessSchedule.ReadyQ.size()-m-1).getPcb().setRqNum(ProcessSchedule.ReadyQ.get(ProcessSchedule.ReadyQ.size()-m-2).getPcb().getRqNum());
								ProcessSchedule.ReadyQ.get(ProcessSchedule.ReadyQ.size()-m-2).getPcb().setRqNum(temp);
							}
						}
					}
				}
			}
			if(ProcessUI.pause) {  //暂停信号
				return;
			}
		}
	}
}
