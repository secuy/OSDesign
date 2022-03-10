package kernel;

import java.util.*;


public class Process {
	private PCB pcb;    //每一进程包含的PCB
	List<Instruction> instructions;   //进程中包含的指令
	private int time_slice;   //剩余时间片时间数
	
	private boolean isLackPage;   //是否缺页标志
	
	public Process() {
		pcb = null;
		instructions = new ArrayList<Instruction>();
		time_slice = -1;
		isLackPage = false;
	}
	public Process(PCB pcb,int time_slice) {
		this.pcb = pcb;
		this.time_slice = time_slice;
		instructions = new ArrayList<Instruction>();
		isLackPage = false;
	}
	public Process(Job j) {  //使用Job构造Process
		PCB pcb = new PCB();   //PCB属性设置
		pcb.setPro_ID(j.getJobsID());
		pcb.setInTimes(j.getInTimes());
		pcb.setInstrucNum(j.getInstrucNum());
		this.pcb = pcb;
		ProcessSchedule.addPcb(pcb);
		
		isLackPage = false;
		this.time_slice = 2;
		instructions = new ArrayList<Instruction>(j.getList());
	}
	public PCB getPcb() {
		return pcb;
	}
	public void setPcb(PCB pcb) {
		this.pcb = pcb;
	}
	public List<Instruction> getInstructions() {
		return instructions;
	}
	public void addInstruction(Instruction ins) {
		instructions.add(ins);
	}
	public int getTime_slice() {
		return time_slice;
	}
	public void setTime_slice(int timeSlice) {
		time_slice = timeSlice;
	}
	public boolean isLackPage() {
		return isLackPage;
	}
	public void setLackPage(boolean isLackPage) {
		this.isLackPage = isLackPage;
	}
	
	
}
