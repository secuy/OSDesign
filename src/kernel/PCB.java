package kernel;

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
	private int lastLeaveBqTime1;  //上一次离开阻塞队列1的时间
	
	private int BqNum2;   //阻塞队列2位置编号
	private List<Integer> BqTimes2;  //进入阻塞队列2时间
	private int lastLeaveBqTime2;  //上一次离开阻塞队列2的时间
	
	private int BqNum3;   //阻塞队列3位置编号
	private List<Integer> BqTimes3;  //进入阻塞队列3时间
	private int lastLeaveBqTime3;  //上一次离开阻塞队列3的时间
	
	private int BqNum4;   //阻塞队列4位置编号
	private List<Integer> BqTimes4;  //进入阻塞队列4时间
	private int lastLeaveBqTime4;  //上一次离开阻塞队列4的时间
	
	private int BqNum5;   //阻塞队列3位置编号
	private List<Integer> BqTimes5;  //进入阻塞队列5时间
	private int lastLeaveBqTime5;  //上一次离开阻塞队列5的时间
	
	/**
	 * 该进程的所有页表项组成的页表，逻辑页号需要5位表示，那么页表项总共有2^5=32个
	 * 
	 */
	private PageItem[] page_items;
	
	private int page_items_num;
	
	
	//分配给该进程的所有页框号（内存中的物理块号）
	//该项可以作为快表使用，加快查询速度
	private PageFrameLRU[] page_frame_nums;
	
	
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
		BqTimes4 = new LinkedList<Integer>();
		BqTimes5 = new LinkedList<Integer>();
		
		lastLeaveBqTime1 = -1;
		lastLeaveBqTime2 = -1;
		lastLeaveBqTime3 = -1;
		lastLeaveBqTime4 = -1;
		lastLeaveBqTime5 = -1;
		
		//初始化页表
		page_items_num = (int)Math.pow(2,PageItem.PAGE_BIT_NUM);
		page_items = new PageItem[page_items_num];
		for(int i=0;i<page_items_num;i++) {
			page_items[i] = new PageItem();
			//设置逻辑地址
			page_items[i].setLogicPageNo(i);
		}
		
		//初始化分配的页框号
		page_frame_nums = new PageFrameLRU[ProcessSchedule.needPageNum];
		
		for(int k=0;k<ProcessSchedule.needPageNum;k++) {
			page_frame_nums[k] = new PageFrameLRU();
		}
		
	}
	

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
	public void addBqTimes4(int time) {
		BqTimes4.add(time);
	}
	public void addBqTimes5(int time) {
		BqTimes5.add(time);
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
	public int getBqTimes4(int i) {
		return BqTimes4.get(i);
	}
	public int getBqTimes5(int i) {
		return BqTimes5.get(i);
	}
	public int getRqLength() {
		return RqTimes.size();
	}
	public int getLastRqTime() {
		return this.getRqTimes(getRqLength()-1);
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
	public int getBq4Length() {
		return BqTimes4.size();
	}
	public int getBq5Length() {
		return BqTimes5.size();
	}
	
	public int getLastBq1Time() {
		return this.getBqTimes1(getBq1Length()-1);
	}
	public int getLastBq2Time() {
		return this.getBqTimes2(getBq2Length()-1);
	}
	public int getLastBq3Time() {
		return this.getBqTimes3(getBq3Length()-1);
	}
	public int getLastBq4Time() {
		return this.getBqTimes4(getBq4Length()-1);
	}
	public int getLastBq5Time() {
		return this.getBqTimes5(getBq5Length()-1);
	}
	
	public int getLastLeaveBqTime1() {
		return lastLeaveBqTime1;
	}


	public void setLastLeaveBqTime1(int lastLeaveBqTime1) {
		this.lastLeaveBqTime1 = lastLeaveBqTime1;
	}


	public int getLastLeaveBqTime2() {
		return lastLeaveBqTime2;
	}


	public void setLastLeaveBqTime2(int lastLeaveBqTime2) {
		this.lastLeaveBqTime2 = lastLeaveBqTime2;
	}


	public int getLastLeaveBqTime3() {
		return lastLeaveBqTime3;
	}


	public void setLastLeaveBqTime3(int lastLeaveBqTime3) {
		this.lastLeaveBqTime3 = lastLeaveBqTime3;
	}


	public int getLastLeaveBqTime4() {
		return lastLeaveBqTime4;
	}


	public void setLastLeaveBqTime4(int lastLeaveBqTime4) {
		this.lastLeaveBqTime4 = lastLeaveBqTime4;
	}


	public int getLastLeaveBqTime5() {
		return lastLeaveBqTime5;
	}


	public void setLastLeaveBqTime5(int lastLeaveBqTime5) {
		this.lastLeaveBqTime5 = lastLeaveBqTime5;
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

	public int getBqNum4() {
		return BqNum4;
	}

	public void setBqNum4(int bqNum4) {
		BqNum3 = bqNum4;
	}
	public int getBqNum5() {
		return BqNum5;
	}

	public void setBqNum5(int bqNum5) {
		BqNum3 = bqNum5;
	}
	
	public void setInterrupt_instruc(Instruction interrupt_instruc) {
		this.interrupt_instruc = interrupt_instruc;
	}

	public Instruction getInterrupt_instruc() {
		return interrupt_instruc;
	}
	

	public PageItem[] getPage_items() {
		return page_items;
	}


	public int getPage_items_num() {
		return page_items_num;
	}
	
	
	
	public PageFrameLRU[] getPage_frame_nums() {
		return page_frame_nums;
	}


	@Override
	public String toString() {
		return "PCB [pro_ID=" + pro_ID + ", inTimes=" + inTimes + ", endTimes="
				+ endTimes + ", instrucNum=" + instrucNum + ", pc=" + pc
				+ ", priority=" + priority + ", psw=" + psw + ", runTimes="
				+ runTimes + ", turnTimes=" + turnTimes + "]";
	}
	
}
