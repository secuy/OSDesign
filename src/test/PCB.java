package test;

import java.util.*;

public class PCB {
	private int pro_ID;   //进程编号
	private int priority;   //进程优先数
	private int inTimes;   //进程创建时间
	private int endTimes;   //进程结束时间
	private int psw;   //该进程当前状态：运行态为0，就绪态为1，阻塞态为2
	private List<Integer> runTimes;    //进程指令运行时间列表
	private int turnTimes;    //进程周转时间统计（ 从进程提交到进程完成的时间间隔为周转时间）
	private int instrucNum;    //指令数目
	private int pc;   //程序计数器，下一条指令编号
	private Instruction instruc;    //当前进程执行指令
	private Instruction interrupt_instruc;  //需要中断处理的指令
	
	private int RqNum;   //就绪队列位置编号
	private List<Integer> RqTimes;   //进入就绪队列时间
	private int BqNum1;   //阻塞队列1位置编号
	private List<Integer> BqTimes1;  //进入阻塞队列1时间
	private int BqNum2;   //阻塞队列2位置编号
	private List<Integer> BqTimes2;  //进入阻塞队列2时间
	private int BqNum3;   //阻塞队列3位置编号
	private List<Integer> BqTimes3;  //进入阻塞队列3时间
	public PCB() {
		this.pro_ID = -1;
		this.priority = -1;
		this.inTimes = -1;
		this.endTimes = -1;
		this.psw = -1;
		this.runTimes = new LinkedList<Integer>();
		this.turnTimes = -1;
		this.instrucNum = -1;
		this.pc = -1;
		this.instruc = null;
		RqTimes = new LinkedList<Integer>();
		BqTimes1 = new LinkedList<Integer>();
		BqTimes2 = new LinkedList<Integer>();
		BqTimes3 = new LinkedList<Integer>();
	}
	
//	public PCB() {
//		
//	}
	public int getPro_ID() {
		return pro_ID;
	}
	public void setPro_ID(int proID) {
		pro_ID = proID;
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
	public int getEndTimes() {
		return endTimes;
	}
	public void setEndTimes(int endTimes) {
		this.endTimes = endTimes;
	}
	public int getPsw() {
		return psw;
	}
	public void setPsw(int psw) {
		this.psw = psw;
	}
	public List<Integer> getRunTimes() {
		return runTimes;
	}
	public void addRunTimes(int run_time) {
		runTimes.add(run_time);
	}
	public int getTurnTimes() {
		return turnTimes;
	}
	public void setTurnTimes(int turnTimes) {
		this.turnTimes = turnTimes;
	}
	public int getInstrucNum() {
		return instrucNum;
	}
	public void setInstrucNum(int instrucNum) {
		this.instrucNum = instrucNum;
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public Instruction getInstruc() {
		return instruc;
	}
	public void setInstruc(Instruction instruc) {
		this.instruc = instruc;
	}
	public void addRqTimes(int time) {
		RqTimes.add(time);
	}
	public void addBqTimes1(int time) {
		BqTimes1.add(time);
	}
	public void addBqTimes2(int time) {
		BqTimes2.add(time);
	}
	public void addBqTimes3(int time) {
		BqTimes3.add(time);
	}
	public int getRqTimes(int i) {
		return RqTimes.get(i);
	}
	public int getBqTimes1(int i) {
		return BqTimes1.get(i);
	}
	public int getBqTimes2(int i) {
		return BqTimes2.get(i);
	}
	public int getBqTimes3(int i) {
		return BqTimes3.get(i);
	}
	public int getRqLength() {
		return RqTimes.size();
	}
	public int getBq1Length() {
		return BqTimes1.size();
	}
	public int getBq2Length() {
		return BqTimes2.size();
	}
	public int getBq3Length() {
		return BqTimes3.size();
	}
	public int getRqNum() {
		return RqNum;
	}

	public void setRqNum(int rqNum) {
		RqNum = rqNum;
	}

	public int getBqNum1() {
		return BqNum1;
	}

	public void setBqNum1(int bqNum1) {
		BqNum1 = bqNum1;
	}

	public int getBqNum2() {
		return BqNum2;
	}

	public void setBqNum2(int bqNum2) {
		BqNum2 = bqNum2;
	}

	public int getBqNum3() {
		return BqNum3;
	}

	public void setBqNum3(int bqNum3) {
		BqNum3 = bqNum3;
	}

	public void setInterrupt_instruc(Instruction interrupt_instruc) {
		this.interrupt_instruc = interrupt_instruc;
	}

	public Instruction getInterrupt_instruc() {
		return interrupt_instruc;
	}
	
}
