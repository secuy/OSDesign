package kernel;


import hardware.Clock;
import java.util.*;
import other.*;


public class JobsRequest extends Thread {  //��ҵ������
	
	OSManage om;
	
	private Clock clock;
	private int lastCheckTime;   //�ϴ���ҵ������ʱ��
	
	//���ļ�����ʱ�������ҵ
	private List<Job> JobsQueue = new LinkedList<Job>();   //��ʱ��ҵ���л�û�в������
	
	private int sumJob;  //��ҵ������,���еõ�������ҵ����
	
	
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
	
	//���������ҵ������
	public int getSumJob() {
		return sumJob;
	}
	
	//�����ҵ�󱸶�������ҵ�ĸ���
	public int getJobsQueueLength() {
		return JobsQueue.size();
	}
	public List<Job> getJobsQueue() {
		return JobsQueue;
	}
	
	
	
	/**
	 * ��������Job������У�������Job����ʱ���������
	 */
	public synchronized void addJob(Job j) {
		int i=0;
		//�ҵ������jobλ��
		for( ;i<JobsQueue.size() && j.getInTimes() >= JobsQueue.get(i).getInTimes();i++);
		JobsQueue.add(i, j);
		this.sumJob++;
		
		
		
	}
	
	/**
	 * ����һ���µ������ҵ�����������ʱ��ҵ���鵱��
	 */
	public synchronized void createANewJob() {  //����һ������ҵ
		Random rand = new Random();
		Job job = new Job(getSumJob()+1,rand.nextInt(5)+1, clock.getTime(), rand.nextInt(30)+1);
		//����һ����Ÿ���ҵָ����ļ�
		IOFile.createRandomInstructionFile(job);
		addJob(job);
	}
	
	
	
	public void run() {
		while(!om.isShutdown()) {
			if(!om.isPause()) {
				if(clock.getTime()%5==0 && clock.getTime()!=lastCheckTime) {
					lastCheckTime = clock.getTime();
					//�����ҵ�Ƿ񵽴��������ҵ�󱸶���
					while(JobsQueue.size()>0 && clock.getTime()>=JobsQueue.get(0).getInTimes()) {  
						//����ʱ���������ҵ
						Job j = JobsQueue.remove(0);
						
						//��ȡ��ҵָ����Ϣ
						IOFile.ReadJobInstructions(j);
						//��ҵ����󱸶���֮��
						ProcessSchedule.getReverseQ().add(j);

						//��Ϣ���
						OSManage.messageOutputSystem(clock.getTime()+":[������ҵ��"+j.getJobsID()+"]");
						
					}
				}
				
				//ʱ�Ӿ���һ��
				clock.passOneSec();
			}
		}
	}
}
