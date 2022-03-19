package kernel;

import java.util.*;


public class Job {
	private int jobsID;  //��ҵ���
	private int priority;  //���ȼ���
	private int inTimes;  //��ҵ����ʱ��
	private int instrucNum;  //��ҵ��������ָ����Ŀ

	private ArrayList<Instruction> list;  //ָ���б�
	
	public Job() {
		this.jobsID = -1;
		this.priority = -1;
		this.inTimes = -1;
		this.instrucNum = 0;
		list = new ArrayList<Instruction>();
	}
	public Job(int jobsID,int priority,int inTimes,int instrucNum) {
		this.jobsID = jobsID;
		this.priority = priority;
		this.inTimes = inTimes;
		this.instrucNum = instrucNum;
		list = new ArrayList<Instruction>();
	}
	
	public int getJobsID() {
		return jobsID;
	}
	public void setJobsID(int jobsID) {
		this.jobsID = jobsID;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getInTimes() {
		return inTimes;
	}
	public void setInTimes(int inTimes) {
		this.inTimes = inTimes;
	}
	public int getInstrucNum() {
		return instrucNum;
	}
	public void setInstrucNum(int instrucNum) {
		this.instrucNum = instrucNum;
	}
	
	
	@Override
	public String toString() {
		return jobsID + "," + priority + "," + inTimes + "," + instrucNum;
	}
	
	
	public ArrayList<Instruction> getList() {
		return list;
	}
	public void setList(ArrayList<Instruction> list) {
		this.list = list;
	}
	public Instruction getJobInstruction(int id) {
		return list.get(id);
	}
	public void setJobInstruction(int id,Instruction i) {
		list.set(id, i);
	}
	public void addJobInstruction(Instruction i) {
		list.add(i);
	}
}
