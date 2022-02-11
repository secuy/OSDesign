package test;

import java.util.*;

public class Job {
	private int jobsID;  //��ҵ���
	private int inTimes;  //��ҵ����ʱ��
	private int instrucNum;  //��ҵ��������ָ����Ŀ
	private ArrayList<Instruction> list;  //ָ���б�
	public Job() {
		list = new ArrayList<Instruction>();
	}
	public Job(int jobsID,int inTimes,int instrucNum) {
		this.jobsID = jobsID;
		this.inTimes = inTimes;
		this.instrucNum = instrucNum;
		list = new ArrayList<Instruction>();
	}
	public int getJobsID() {
		return jobsID;
	}
	public int getInTimes() {
		return inTimes;
	}
	public int getInstrucNum() {
		return instrucNum;
	}
	public void setJobsID(int jobsID) {
		this.jobsID = jobsID;
	}
	public void setInTimes(int inTimes) {
		this.inTimes = inTimes;
	}
	public void setInstrucNum(int instrucNum) {
		this.instrucNum = instrucNum;
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
