package test;
import java.util.*;

public class JobsRequest extends Thread {  //��ҵ������
	private static Clock clock;
	private int lastCheckTime;   //�ϴ���ҵ������ʱ��
	private static List<Job> JobsQueue = new LinkedList<Job>();   //��ҵ����
	private static int sumJob = 0;  //��ҵ������
	
	public JobsRequest() {
		clock = new Clock();
		lastCheckTime = -1;
	}
	public static void jobInQueue(Job j) {  //��ҵ���
		JobsQueue.add(j);
		addSumJob();
	}
	public static Job jobOutQueue() {  //��ͷ����
		return JobsQueue.remove(0);
	}
	public static Job getJobHead() {  //��ö�ͷ��ҵ
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
	public static Job createANewJob(int instrucNum) {  //����һ������ҵ
		Job job = new Job(getSumJob()+1, clock.getTime(), instrucNum);
		for(int i=0;i<instrucNum;i++) {
			Instruction ins = new Instruction(i+1,new Random().nextInt(4));
			job.addJobInstruction(ins);
		}
		return job;
	}
	public static void addJob(Job j) {  //��������Job������У�������Job����ʱ���������
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
				//��ҵ��鲢�������̽����������
				int i=0;
				while(JobsQueue.get(0)!=null && clock.getTime()>=JobsQueue.get(0).getInTimes()) {  
					Job j = JobsQueue.remove(0);
					System.out.println(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
					IOFile.writeMessageInData(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
					ProcessUI.addMessage(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
					
					ProcessSchedule.addReadyQ(ProcessSchedule.createProcess(j));
					i++;
					//����ͬһʱ���������ҵ�����ȼ�ִ���Ⱥ�˳��
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
			if(ProcessUI.pause) {  //��ͣ�ź�
				return;
			}
		}
	}
}
