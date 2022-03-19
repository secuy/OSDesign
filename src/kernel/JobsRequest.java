package kernel;


import hardware.Clock;
import java.util.*;
import other.*;


public class JobsRequest extends Thread {  //作业请求类
	
	OSManage om;
	
	private Clock clock;
	private int lastCheckTime;   //上次作业请求检查时间
	
	//从文件中临时读入的作业
	private List<Job> JobsQueue = new LinkedList<Job>();   //临时作业队列还没有参与调度
	
	private int sumJob;  //作业的总数,所有得到过的作业总数
	
	
	public JobsRequest(OSManage om) {
		this.om = om;
		
		clock = new Clock();
		lastCheckTime = -1;
		sumJob = 0;
		List<Job> joblist = IOFile.ReadJobsList();
		
		for(int i=0;i<joblist.size();i++) {
			addJob(joblist.get(i));
		}
		
	}
	
	//获得所有作业的总数
	public int getSumJob() {
		return sumJob;
	}
	
	//获得作业后备队列中作业的个数
	public int getJobsQueueLength() {
		return JobsQueue.size();
	}
	public List<Job> getJobsQueue() {
		return JobsQueue;
	}
	
	
	
	/**
	 * 将创建的Job加入队列，并根据Job进入时间进行排序
	 */
	public synchronized void addJob(Job j) {
		int i=0;
		//找到加入的job位置
		for( ;i<JobsQueue.size() && j.getInTimes() >= JobsQueue.get(i).getInTimes();i++);
		JobsQueue.add(i, j);
		this.sumJob++;
		
		
		
	}
	
	/**
	 * 创建一个新的随机作业并将其放入临时作业数组当中
	 */
	public synchronized void createANewJob() {  //创建一个新作业
		Random rand = new Random();
		Job job = new Job(getSumJob()+1,rand.nextInt(5)+1, clock.getTime(), rand.nextInt(30)+1);
		//创建一个存放该作业指令的文件
		IOFile.createRandomInstructionFile(job);
		addJob(job);
	}
	
	
	
	public void run() {
		while(!om.isShutdown()) {
			if(!om.isPause()) {
				if(clock.getTime()%5==0 && clock.getTime()!=lastCheckTime) {
					lastCheckTime = clock.getTime();
					//检查作业是否到达，并进入作业后备队列
					while(JobsQueue.size()>0 && clock.getTime()>=JobsQueue.get(0).getInTimes()) {  
						//从临时数组读出作业
						Job j = JobsQueue.remove(0);
						
						//读取作业指令信息
						IOFile.ReadJobInstructions(j);
						//作业进入后备队列之后，
						ProcessSchedule.getReverseQ().add(j);

						//信息输出
						OSManage.messageOutputSystem(clock.getTime()+":[新增作业："+j.getJobsID()+"]");
						
					}
				}
				
				//时钟经过一秒
				clock.passOneSec();
			}
		}
	}
}
