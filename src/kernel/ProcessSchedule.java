package kernel;

import hardware.CPU;
import hardware.Clock;
import hardware.MMU;

import java.util.*;

import other.IOFile;
import other.OSManage;

import ui.ProcessUI;




public class ProcessSchedule extends Thread{  //���̵�����
	private static Clock clock;   	//ʱ��
	private static Process runningProcess = null;    //����ִ�еĽ���
	
	//���ó���������û����̲�����Ϊ8
	public static final int MAX_PROCESS_NUM = 8;
	
	//������ʱ��Ƭ��СΪ2s
	public static final int TIME_SLICE = 2;
	
	//������ÿ���û�����ռ�õ����������ҳ����Ϊ3
	public static final int needPageNum = 3;
	
	//��������������״̬������̬
	public static final int RUNNING_STATE = 0;
	
	//��������������״̬������̬
	public static final int READY_STATE = 1;
	
	//��������������״̬������̬
	public static final int BLOCK_STATE = 2;
	
	
	
	//λ�����ĺ󱸶���
	//ʹ��Vector�������̰߳�ȫ�ģ���ͬ�̶߳�������������
	private static Vector<Job> ReverseQ = new Vector<Job>();
	
	private static Vector<Process> ReadyQ = new Vector<Process>();   //��������
	
	private static Vector<Process> BlockQ1 = new Vector<Process>();   //��������1
	private static Vector<Process> BlockQ2 = new Vector<Process>();   //��������2
	private static Vector<Process> BlockQ3 = new Vector<Process>();   //��������3
	private static Vector<Process> BlockQ4 = new Vector<Process>();   //��������4
	private static Vector<Process> BlockQ5 = new Vector<Process>();   //��������5
	
	private static Vector<Process> FinishedQ = new Vector<Process>();   //����ִ����ɶ���
	
	
	private static Vector<PCB> pcbList = new Vector<PCB>();   //�����ڴ��е�PCB��
	
	public ProcessSchedule() {
		clock = new Clock();
	}
	
	public static Process getRunningProcess() {
		return runningProcess;
	}
	public static void setRunningProcess(Process runningProcess) {
		ProcessSchedule.runningProcess = runningProcess;
	}
	public static void addPcb(PCB pcb) {
		pcbList.add(pcb);
	}
	
	
	
	public static Vector<PCB> getPcbList() {
		return pcbList;
	}

	public static List<Job> getReverseQ() {
		return ReverseQ;
	}

	public static List<Process> getReadyQ() {
		return ReadyQ;
	}

	public static List<Process> getBlockQ1() {
		return BlockQ1;
	}

	public static List<Process> getBlockQ2() {
		return BlockQ2;
	}

	public static List<Process> getBlockQ3() {
		return BlockQ3;
	}

	public static List<Process> getBlockQ4() {
		return BlockQ4;
	}
	public static List<Process> getBlockQ5() {
		return BlockQ5;
	}
	
	public static List<Process> getFinishedQ() {
		return FinishedQ;
	}

	//ֻ������������к���������ʱ�Ż��н���̬��ת�䡢�����������ʱ�䡢�������б�ű仯�������������Ƕ��б�ŵı仯
	public static void addReadyQ(Process p) {  //���̼����������
		p.getPcb().setRqNum(ReadyQ.size());  //���þ��������еı��
		p.getPcb().addRqTimes(clock.getTime());  //�����������ʱ��
		p.getPcb().setPsw(1);  //����Ϊ����̬
		ReadyQ.add(p);
	}
	public static void addBlockQ1(Process p) {  //���̼�����������1
		p.getPcb().setBqNum1(BlockQ1.size());
		p.getPcb().addBqTimes1(clock.getTime());
		p.getPcb().setPsw(2);  //����Ϊ����̬
		BlockQ1.add(p);
	}
	public static void addBlockQ2(Process p) {  //���̼�����������2
		p.getPcb().setBqNum2(BlockQ2.size());
		p.getPcb().addBqTimes2(clock.getTime());
		p.getPcb().setPsw(2);  //����Ϊ����̬
		BlockQ2.add(p);
	}
	public static void addBlockQ3(Process p) {  //���̼�����������3
		p.getPcb().setBqNum3(BlockQ3.size());
		p.getPcb().addBqTimes3(clock.getTime());
		p.getPcb().setPsw(2);  //����Ϊ����̬
		BlockQ3.add(p);
	}
	public static void addBlockQ4(Process p) {  //���̼�����������3
		p.getPcb().setBqNum4(BlockQ4.size());
		p.getPcb().addBqTimes4(clock.getTime());
		p.getPcb().setPsw(2);  //����Ϊ����̬
		BlockQ4.add(p);
	}
	public static void addBlockQ5(Process p) {  //���̼�����������3
		p.getPcb().setBqNum5(BlockQ5.size());
		p.getPcb().addBqTimes5(clock.getTime());
		p.getPcb().setPsw(2);  //����Ϊ����̬
		BlockQ5.add(p);
	}
	public static Process popReadyQ() {  //������ͷ����
		Process p = ReadyQ.remove(0);
		for(int i=0;i<ReadyQ.size();i++) {  //���¶��б����Ϣ
			ReadyQ.get(i).getPcb().setRqNum(i);
		}
		return p;
	}
	public static Process popBlockQ1() {  //������ͷ����
		Process p = BlockQ1.remove(0);
		for(int i=0;i<BlockQ1.size();i++) {  //���¶��б����Ϣ
			BlockQ1.get(i).getPcb().setBqNum1(i);
		}
		return p;
	}
	public static Process popBlockQ2() {  //������ͷ����
		Process p = BlockQ2.remove(0);
		for(int i=0;i<BlockQ2.size();i++) {  //���¶��б����Ϣ
			BlockQ2.get(i).getPcb().setBqNum2(i);
		}
		return p;
	}
	public static Process popBlockQ3() {  //������ͷ����
		Process p = BlockQ3.remove(0);
		for(int i=0;i<BlockQ3.size();i++) {  //���¶��б����Ϣ
			BlockQ3.get(i).getPcb().setBqNum3(i);
		}
		return p;
	}
	public static Process popBlockQ4() {  //������ͷ����
		Process p = BlockQ4.remove(0);
		for(int i=0;i<BlockQ4.size();i++) {  //���¶��б����Ϣ
			BlockQ4.get(i).getPcb().setBqNum4(i);
		}
		return p;
	}
	public static Process popBlockQ5() {  //������ͷ����
		Process p = BlockQ5.remove(0);
		for(int i=0;i<BlockQ5.size();i++) {  //���¶��б����Ϣ
			BlockQ5.get(i).getPcb().setBqNum5(i);
		}
		return p;
	}
	public static void cpuStateProtection() {   //cpu�Ĵ����ֳ�����
		runningProcess.getPcb().setPc(CPU.getPc());
		runningProcess.getPcb().setInstruc(CPU.getIr());
	}
	public static void cpuStateRecovery() {   //cpu�ֳ��ָ�
		CPU.setPc(runningProcess.getPcb().getPc());
		CPU.setIr(runningProcess.getPcb().getInstruc());
	}
	//���̿���ԭ��
	public static Process createProcess(Job j) {  //������ҵ��������ԭ�����������е���
		System.out.println(clock.getTime()+":[��������"+j.getJobsID()+"]");
		//IOFile.writeMessageInData(clock.getTime()+":[��������"+j.getJobsID()+"]");
		//ProcessUI.addMessage(clock.getTime()+":[��������"+j.getJobsID()+"]");
		
		Process p = new Process(j);
		p.setTime_slice(TIME_SLICE);   //���ô�������ʱ��ƬΪ�涨ʱ��Ƭ��ʱ��
		p.getPcb().setInstruc(null);  //��ǰִ��ָ��Ϊ���1ָ��
		p.getPcb().setPc(1);  //��һ��ָ����Ϊ2
		
		//�����ڴ�ҳ��
		MMU.givePageFrame(p.getPcb());
		
		Random r = new Random();
		p.getPcb().setPriority(r.nextInt(5)+1);   //�����������Ϊ�����е����ȼ���
		
		//���ý��̷��뵽�������е���
		addReadyQ(p);
		
		//ProcessUI.addProcessMessage(clock.getTime()+"s����"+p.getPcb().getPro_ID()+"���룬���ȼ�Ϊ��"+p.getPcb().getPriority()+"ָ������Ϊ��"+p.getPcb().getInstrucNum());
		return p;
	}
	public static void cancelProcess(Process p) {  //������ɵĽ���
		p.getPcb().setEndTimes(clock.getTime());
		p.getPcb().setTurnTimes(p.getPcb().getEndTimes()-p.getPcb().getInTimes());
		p.getPcb().setPsw(-1);
		//��PCB�б����Ƴ�PCB
		for(int i=0;i<pcbList.size();i++) {
			if(pcbList.get(i).getPro_ID() == p.getPcb().getPro_ID()) {
				pcbList.remove(i);
			}
		}
		FinishedQ.add(p);
		System.out.println(clock.getTime()+":[��ֹ����"+p.getPcb().getPro_ID()+"]");
		//IOFile.writeMessageInData(clock.getTime()+":[��ֹ����"+p.getPcb().getPro_ID()+"]");
		//ProcessUI.addMessage(clock.getTime()+":[��ֹ����"+p.getPcb().getPro_ID()+"]");
		//ProcessUI.addProcessMessage(clock.getTime()+"s����"+p.getPcb().getPro_ID()+"ִ�����"+"����ʱ��"+p.getPcb().getTurnTimes());
	}
	public static void blockProcess(Process p) {  //��������
		//���ȱҳ��������������3��
		if(p.isLackPage()) {
			addBlockQ3(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
			return;
		}
		
		//����ָ��״̬������������
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==2) {
			addBlockQ1(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������1]");
			//IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������1]");
			//ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������1]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==3) {
			addBlockQ2(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������2]");
			//IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������2]");
			//ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������2]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==4) {
			addBlockQ3(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
			//IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
			//ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==5) {
			addBlockQ4(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������4]");
			//IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������4]");
			//ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������4]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==6) {
			addBlockQ5(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������5]");
			//IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������5]");
			//ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������5]");
		}
		runningProcess.setTime_slice(0);  //ʹ��ʱ��Ƭ������
		
	}
	public static void awakeProcess(int BlockQNum,int now_time) {  //���ѽ��̣�����Ϊ����������ţ������������ж�ͷ����
		switch(BlockQNum) {
			case 2:
				Process p1 = popBlockQ1();   //���������У����������
				p1.getPcb().setPsw(1);
				p1.setTime_slice(2);
				addReadyQ(p1);
				System.out.println(now_time+":[���ѽ���"+p1.getPcb().getPro_ID()+"�������1]");
				//IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p1.getPcb().getPro_ID()+"�������1]");
				//ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p1.getPcb().getPro_ID()+"�������1]");
				break;
			case 3:
				Process p2 = popBlockQ2();   //���������У����������
				p2.getPcb().setPsw(1);
				p2.setTime_slice(2);
				addReadyQ(p2);
				System.out.println(now_time+":[���ѽ���"+p2.getPcb().getPro_ID()+"�������1]");
				//IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p2.getPcb().getPro_ID()+"�������1]");
				//ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p2.getPcb().getPro_ID()+"�������1]");
				break;
			case 4:
				Process p3 = popBlockQ3();
				p3.getPcb().setPsw(1);
				p3.setTime_slice(2);
				addReadyQ(p3);
				System.out.println(now_time+":[���ѽ���"+p3.getPcb().getPro_ID()+"�������1]");
				//IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p3.getPcb().getPro_ID()+"�������1]");
				//ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p3.getPcb().getPro_ID()+"�������1]");
				break;
			case 5:
				Process p4 = popBlockQ4();
				p4.getPcb().setPsw(1);
				p4.setTime_slice(2);
				addReadyQ(p4);
				System.out.println(now_time+":[���ѽ���"+p4.getPcb().getPro_ID()+"�������1]");
				//IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p4.getPcb().getPro_ID()+"�������1]");
				//ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p4.getPcb().getPro_ID()+"�������1]");
				break;
			case 6:
				Process p5 = popBlockQ5();
				p5.getPcb().setPsw(1);
				p5.setTime_slice(2);
				addReadyQ(p5);
				System.out.println(now_time+":[���ѽ���"+p5.getPcb().getPro_ID()+"�������1]");
				//IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p5.getPcb().getPro_ID()+"�������1]");
				//ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p5.getPcb().getPro_ID()+"�������1]");
				break;
		}
			
	}
	
	//�Ծ��������еĽ��̰����ȼ������д�С���������
	public void sortReadyQ() {
		int min_priority,min_index;
		for(int i=0;i<ReadyQ.size()-1;i++) {
			min_priority=99; min_index=i;
			for(int k=i;k<ReadyQ.size();k++) {
				if(ReadyQ.get(k).getPcb().getPriority() < min_priority) {
					min_priority = ReadyQ.get(k).getPcb().getPriority();
					min_index = k;
				}
			}
			Process temp = ReadyQ.get(i);
			ReadyQ.set(i, ReadyQ.get(min_index));
			ReadyQ.set(min_index, temp);
		}
	}
	
	//�߼����ȣ�������еĺ󱸶����д������̵��ڴ��еľ�������
	public void highSchedule() {
		//����󱸶�����û����ҵ�����ߵ�ǰ���ڴ��е�PCB�������Ѿ�������󲢷����������Ͳ����и߼�����
		if(ReverseQ.size()<=0 || pcbList.size()>=MAX_PROCESS_NUM) {
			return;
		}
		
		//�ҵ����ȼ�������ҵ�����ȼ�����С����˳����뵽�������е���
		int min_priority,min_index;
		while(ReverseQ.size()>0 && pcbList.size()<MAX_PROCESS_NUM) {
			min_priority=100;
			min_index=0;
			for(int i=0;i<ReverseQ.size();i++) {
				if(ReverseQ.get(i).getPriority() < min_priority) {
					min_index = i;
					min_priority = ReverseQ.get(i).getPriority();
				}
			}
			createProcess(ReverseQ.remove(min_index));
		}
	}
	
	//�ͼ����ȣ��ڴ���cpu֮��ĵ���
	public void lowSchedule() {
		//�����������Ϊ�ջ��ߣ�������ִ�еĳ�����ôֱ�ӽ����ͼ�����
		if(ReadyQ.size()==0 || runningProcess!=null) {
			return;
		}
		
		//����ǰ�ȶ����ȼ���������
		sortReadyQ();
		
		//��CPU�л�Ϊ�ں�̬
		CPU.switchToKernelState();
		
		//�������ж�ͷԪ�س���
		runningProcess = popReadyQ();
		//����Ϊ����״̬
		runningProcess.getPcb().setPsw(RUNNING_STATE);
		//�ָ�cpu�ֳ�
		cpuStateRecovery();
		//��������ʱ��
		runningProcess.getPcb().addRunTimes(clock.getTime());
		//��CPU�л�Ϊ�û�̬
		CPU.switchToUserState();
		
	}
	
	public void run() {
		
		while(true) {
			
			highSchedule();
			lowSchedule();
			
			
			if(runningProcess!=null) {
				
				
				//�л���һ��ָ��
				CPU.nextInstruction(runningProcess);
				
				//ȱҳ�жϼ��
				if(LackPageInterrupt.checkLackPage(runningProcess)) {
					//ȱҳ��ֱ���������沽��
					continue;
				}
				
				//CPUִ��ָ��
				CPU.excute();
				
				//����Ƿ���Ҫ�ж�
				InterruptOperator.inInterrupt();
			}
			
			//ʱ�Ӿ���һ��
			clock.passOneSec();
			
			
			//�����ָ��û����ɣ���ô���ܽ��н��̵��ȣ���Ҫֱ�Ӽ������
			if(runningProcess!=null && !runningProcess.getPcb().getInstruc().isFinished()) {
				continue;
			}
			
			//���ִ�е������һ��ָ��
			if(runningProcess!=null && runningProcess.getPcb().getPc()>runningProcess.getPcb().getInstrucNum()) {
				cancelProcess(runningProcess);
				runningProcess = null;
				continue;
			}
			
			if(runningProcess!=null) {
				//ʱ��Ƭû������
				if(runningProcess.getTime_slice()>1) {
					runningProcess.setTime_slice(runningProcess.getTime_slice()-1);
				} else {
					//ʱ��Ƭ����
					cpuStateProtection();  //cpu�ֳ�����
					runningProcess.setTime_slice(2);  //�ָ�ʱ��Ƭ��ʱ��
					if(ReadyQ.size()==0) {  //����������Ϊ�գ������ִ�е�ǰ����
						continue;
					}
					runningProcess.getPcb().setPsw(READY_STATE);
					addReadyQ(runningProcess);   //����ǰ���̼����������
					runningProcess = null;
				}
			}
			
		}
		
		/*while(true) {
			try {
				sleep(100);  //�����ȴ���ʹJob����
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(FinishedQ.size() == pcbList.size() && JobsRequest.getJobsLength()==0) {  //���������ҵ,�򽫽�������Ϣ������ļ���
				IOFile.outputMessageToFile(clock.getTime()-1);
				return;
			}
			InterruptOperator.checkInterruptOperator();   //����Ƿ����жϴ������
			if(!CPU.isIs_break_close()) {
				if(runningProcess!=null && runningProcess.getTime_slice()>0) {  //ʱ��Ƭû������
					if(runningProcess.getPcb().getPc()>runningProcess.getPcb().getInstrucNum()) {
						cancelProcess(runningProcess);
						runningProcess = null;
						continue;
					}
					CPU.nextInstruction(runningProcess);
					CPU.excute();
					runningProcess.setTime_slice(runningProcess.getTime_slice()-1);
					InterruptOperator.inInterrupt();   //�鿴��ǰָ���Ƿ���Ҫϵͳ�жϵ���
					continue;  //����CPUִ�й�1sֱ�ӽ�����һѭ��
					//�����л�
				} else if(runningProcess!=null && runningProcess.getTime_slice()<=0) {  //ʱ��Ƭ����,�л����������еĶ�ͷ����
					cpuStateProtection();  //cpu�ֳ�����
					runningProcess.setTime_slice(2);  //�ָ�ʱ��Ƭ��ʱ��
					if(ReadyQ.size()==0) {  //����������Ϊ�գ������ִ�е�ǰ����
						continue;
					}
					addReadyQ(runningProcess);   //����ǰ���̼����������  
					runningProcess.setTime_slice(2);  //�ָ�ʱ��Ƭ��ʱ��
					
					runningProcess = popReadyQ();  //���������ж�ͷ����ȡ��תΪִ��״̬
					runningProcess.getPcb().setPsw(0);   //����Ϊ����̬
					cpuStateRecovery();  //cpu�ֳ��ָ�
					runningProcess.getPcb().addRunTimes(clock.getTime());
					continue;
				} else if(runningProcess==null && ReadyQ.size()>0) {
					//��cpu������û�й��ж��Լ���ͷ����ʱ��Ƭ������ȡ���������ж�ͷ����
					runningProcess = popReadyQ();  //ȡ����ͷ����
					runningProcess.getPcb().addRunTimes(clock.getTime());
					runningProcess.getPcb().setPsw(0);
					cpuStateRecovery();
					CPU.setIs_busy(true);
					continue;
				}
			}
			try {  //ֻ��CPU����ҵ���߹��ж�ʱʹ������ʱ�����
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ProcessUI.OutputMessage();
			clock.passOneSec();
			if(ProcessUI.isPause()) {  //��ͣ�ź�
				return;
			}
		}*/
	}
}
