package kernel;

import java.util.*;


public class Job {
	private int jobsID;  //作业序号
	private int priority;  //优先级数
	private int inTimes;  //作业请求时间
	private int instrucNum;  //作业包含程序指令数目

	private ArrayList<Instruction> list;  //指令列表
	
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
