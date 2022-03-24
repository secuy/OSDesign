package kernel;

import java.util.*;


public class PCB {
	private int pro_ID;   //���̱��
	private int priority;   //����������
	private int inTimes;   //���̴���ʱ��
	private int endTimes;   //���̽���ʱ��
	private int psw;   //�ý��̵�ǰ״̬������̬Ϊ0������̬Ϊ1������̬Ϊ2
	private List<Integer> runTimes;    //����ָ������ʱ���б�
	private int turnTimes;    //������תʱ��ͳ�ƣ� �ӽ����ύ��������ɵ�ʱ����Ϊ��תʱ�䣩
	private int instrucNum;    //ָ����Ŀ
	private int pc;   //�������������һ��ָ����
	private Instruction instruc;    //��ǰ����ִ��ָ��
	private Instruction interrupt_instruc;  //��Ҫ�жϴ����ָ��
	
	private int RqNum;   //��������λ�ñ��
	private List<Integer> RqTimes;   //�����������ʱ��
	private int BqNum1;   //��������1λ�ñ��
	private List<Integer> BqTimes1;  //������������1ʱ��
	private int lastLeaveBqTime1;  //��һ���뿪��������1��ʱ��
	
	private int BqNum2;   //��������2λ�ñ��
	private List<Integer> BqTimes2;  //������������2ʱ��
	private int lastLeaveBqTime2;  //��һ���뿪��������2��ʱ��
	
	private int BqNum3;   //��������3λ�ñ��
	private List<Integer> BqTimes3;  //������������3ʱ��
	private int lastLeaveBqTime3;  //��һ���뿪��������3��ʱ��
	
	private int BqNum4;   //��������4λ�ñ��
	private List<Integer> BqTimes4;  //������������4ʱ��
	private int lastLeaveBqTime4;  //��һ���뿪��������4��ʱ��
	
	private int BqNum5;   //��������3λ�ñ��
	private List<Integer> BqTimes5;  //������������5ʱ��
	private int lastLeaveBqTime5;  //��һ���뿪��������5��ʱ��
	
	/**
	 * �ý��̵�����ҳ������ɵ�ҳ���߼�ҳ����Ҫ5λ��ʾ����ôҳ�����ܹ���2^5=32��
	 * 
	 */
	private PageItem[] page_items;
	
	private int page_items_num;
	
	
	//������ý��̵�����ҳ��ţ��ڴ��е������ţ�
	//���������Ϊ���ʹ�ã��ӿ��ѯ�ٶ�
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
		
		//��ʼ��ҳ��
		page_items_num = (int)Math.pow(2,PageItem.PAGE_BIT_NUM);
		page_items = new PageItem[page_items_num];
		for(int i=0;i<page_items_num;i++) {
			page_items[i] = new PageItem();
			//�����߼���ַ
			page_items[i].setLogicPageNo(i);
		}
		
		//��ʼ�������ҳ���
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
