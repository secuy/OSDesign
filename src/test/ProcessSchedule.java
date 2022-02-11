package test;

import java.util.*;

public class ProcessSchedule extends Thread{  //���̵�����
	private static Clock clock;   	//ʱ��
	private static Process runningProcess = null;    //����ִ�еĽ���
	static List<Process> ReadyQ = new LinkedList<Process>();   //��������
	static List<Process> BlockQ1 = new LinkedList<Process>();   //��������1
	static List<Process> BlockQ2 = new LinkedList<Process>();   //��������2
	static List<Process> BlockQ3 = new LinkedList<Process>();   //��������3
	static List<Process> FinishedQ = new LinkedList<Process>();   //����ִ����ɶ���
	private static List<PCB> pcbList = new ArrayList<PCB>();   //PCB��
	
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
	public static void cpuStateProtection() {   //cpu�Ĵ����ֳ�����
		runningProcess.getPcb().setPc(CPU.getPc());
		runningProcess.getPcb().setInstruc(CPU.getIr());
	}
	public static void cpuStateRecovery() {   //cpu�ֳ��ָ�
		CPU.setPc(runningProcess.getPcb().getPc());
		CPU.setIr(runningProcess.getPcb().getInstruc());
	}
	//���̿���ԭ��
	public static Process createProcess(Job j) {  //������ҵ��������ԭ��
		System.out.println(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
		IOFile.writeMessageInData(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
		ProcessUI.addMessage(clock.getTime()+":[������ҵ"+j.getJobsID()+"]");
		
		Process p = new Process(j);
		p.setTime_slice(2);   //���ô�������ʱ��ƬΪ2�� 
		p.getPcb().setInstruc(null);  //��ǰִ��ָ��Ϊ���1ָ��
		p.getPcb().setPc(1);  //��һ��ָ����Ϊ2
		Random r = new Random();
		p.getPcb().setPriority(r.nextInt(5)+1);   //�����������Ϊ�����е����ȼ���
		
		ProcessUI.addProcessMessage(clock.getTime()+"s����"+p.getPcb().getPro_ID()+"���룬���ȼ�Ϊ��"+p.getPcb().getPriority()+"ָ������Ϊ��"+p.getPcb().getInstrucNum());
		return p;
	}
	public static void cancelProcess(Process p) {  //������ɵĽ���
		p.getPcb().setEndTimes(clock.getTime());
		p.getPcb().setTurnTimes(p.getPcb().getEndTimes()-p.getPcb().getInTimes());
		p.getPcb().setPsw(-1);
		FinishedQ.add(p);
		System.out.println(clock.getTime()+":[��ֹ����"+p.getPcb().getPro_ID()+"]");
		IOFile.writeMessageInData(clock.getTime()+":[��ֹ����"+p.getPcb().getPro_ID()+"]");
		ProcessUI.addMessage(clock.getTime()+":[��ֹ����"+p.getPcb().getPro_ID()+"]");
		ProcessUI.addProcessMessage(clock.getTime()+"s����"+p.getPcb().getPro_ID()+"ִ�����"+"����ʱ��"+p.getPcb().getTurnTimes());
	}
	public static void blockProcess(Process p) {  //��������
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==1) {
			addBlockQ1(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������1]");
			IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������1]");
			ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������1]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==2) {
			addBlockQ2(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������2]");
			IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������2]");
			ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������2]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==3) {
			addBlockQ3(p);
			System.out.println(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
			IOFile.writeMessageInData(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
			ProcessUI.addMessage(clock.getTime()+":[��������"+p.getPcb().getPro_ID()+"�������3]");
		}
		runningProcess.setTime_slice(0);  //ʹ��ʱ��Ƭ������
	}
	public static void awakeProcess(int BlockQNum) {  //���ѽ��̣�����Ϊ����������ţ������������ж�ͷ����
		switch(BlockQNum) {
			case 1:
				Process p1 = popBlockQ1();   //���������У����������
				p1.getPcb().setPsw(1);
				p1.setTime_slice(2);
				addReadyQ(p1);
				System.out.println(clock.getTime()+":[���ѽ���"+p1.getPcb().getPro_ID()+"�������1]");
				IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p1.getPcb().getPro_ID()+"�������1]");
				ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p1.getPcb().getPro_ID()+"�������1]");
				break;
			case 2:
				Process p2 = popBlockQ2();   //���������У����������
				p2.getPcb().setPsw(1);
				p2.setTime_slice(2);
				addReadyQ(p2);
				System.out.println(clock.getTime()+":[���ѽ���"+p2.getPcb().getPro_ID()+"�������1]");
				IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p2.getPcb().getPro_ID()+"�������1]");
				ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p2.getPcb().getPro_ID()+"�������1]");
				break;
			case 3:
				Process p3 = popBlockQ3();
				p3.getPcb().setPsw(1);
				p3.setTime_slice(2);
				addReadyQ(p3);
				System.out.println(clock.getTime()+":[���ѽ���"+p3.getPcb().getPro_ID()+"�������1]");
				IOFile.writeMessageInData(clock.getTime()+":[���ѽ���"+p3.getPcb().getPro_ID()+"�������1]");
				ProcessUI.addMessage(clock.getTime()+":[���ѽ���"+p3.getPcb().getPro_ID()+"�������1]");
				break;
		}
			
	}
	public void run() {
		while(true) {
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
			if(ProcessUI.pause) {  //��ͣ�ź�
				return;
			}
		}
	}
}
