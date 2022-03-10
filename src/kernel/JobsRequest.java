package kernel;


import hardware.Clock;
import java.util.*;
import other.*;
import ui.ProcessUI;


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
	public void addJob(Job j) {
		int i=0;
		//�ҵ������jobλ��
		for( ;i<JobsQueue.size() && j.getInTimes() >= JobsQueue.get(i).getInTimes();i++);
		JobsQueue.add(i, j);
		this.sumJob++;
	}
	
	/*public Job createANewJob(int instrucNum) {  //����һ������ҵ
		Job job = new Job(getSumJob()+1, clock.getTime(), instrucNum);
		for(int i=0;i<instrucNum;i++) {
			Instruction ins = new Instruction(i+1,new Random().nextInt(4));
			job.addJobInstruction(ins);
		}
		return job;
	}*/
	
	
	
	public void run() {
		while(true) {
			if(clock.getTime()%5==0 && clock.getTime()!=lastCheckTime) {
				lastCheckTime = clock.getTime();
				//�����ҵ�Ƿ񵽴��������ҵ�󱸶���
				while(JobsQueue.size()>0 && clock.getTime()>=JobsQueue.get(0).getInTimes()) {  
					//����ʱ���������ҵ
					Job j = JobsQueue.remove(0);
					//IOFile.writeMessageInData(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
					//ProcessUI.addMessage(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
					
					//��ȡ��ҵָ����Ϣ
					IOFile.ReadJobInstructions(j);
					//��ҵ����󱸶���֮��
					ProcessSchedule.getReverseQ().add(j);
					System.out.println(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
				}
			}
			
			//ʱ�Ӿ���һ��
			clock.passOneSec();
			
			
			
			if(ProcessUI.isPause()) {  //��ͣ�ź�
				return;
			}
		}
	}
}
