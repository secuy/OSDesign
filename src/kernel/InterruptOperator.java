package kernel;

import hardware.*;


public class InterruptOperator {   //�жϴ���
	
	private static Instruction keyboard_interrupt = null;  //�����ж�ָ���Ӧָ������Ϊ2,��������4,Ϊ��˵������
	private static int keyboard_time = -1;  //��������жϴ����ʱ�䣨ע���������������ʱ�䲻ͬ��
	
	private static Instruction screen_interrupt = null;  //��Ļ�жϣ���Ӧָ������Ϊ3,��������2
	private static int screen_time = -1;  //������Ļ�жϴ����ʱ��
	
	private static Instruction readDisk_interrupt = null;  //�������ļ��жϣ���Ӧָ������Ϊ4,��������3
	private static int readDisk_time = -1;  //�������ļ���ʱ��
	
	private static Instruction writeDisk_interrupt = null;  //д�����ļ��жϣ���Ӧָ������Ϊ5,��������4
	private static int writeDisk_time = -1;  //д�����ļ���ʱ��
	
	private static Instruction print_interrupt = null;  //��ӡ�����жϣ���Ӧָ������Ϊ6,��������5
	private static int print_time = -1;  //��ӡ������ʱ��
	
	public InterruptOperator() {
		
	}
	public static int getKeyboard_time() {
		return keyboard_time;
	}
	public static void setKeyboard_time(int keyboardTime) {
		keyboard_time = keyboardTime;
	}
	public static int getScreen_time() {
		return screen_time;
	}
	public static void setScreen_time(int screenTime) {
		screen_time = screenTime;
	}
	
	public static int getReadDisk_time() {
		return readDisk_time;
	}
	public static void setReadDisk_time(int readDiskTime) {
		readDisk_time = readDiskTime;
	}
	public static int getWriteDisk_time() {
		return writeDisk_time;
	}
	public static void setWriteDisk_time(int writeDiskTime) {
		writeDisk_time = writeDiskTime;
	}
	public static int getPrint_time() {
		return print_time;
	}
	public static void setPrint_time(int printTime) {
		print_time = printTime;
	}
	
	public static Instruction getScreen() {
		return screen_interrupt;
	}
	public synchronized static void inInterrupt() {  //�����жϴ���
		if(CPU.getIr().getInstruc_state()==0 || CPU.getIr().getInstruc_state()==1) {
			if(!ProcessSchedule.getRunningProcess().isLackPage()) {
				return;
			}
		}
		ProcessSchedule.getRunningProcess().getPcb().setInterrupt_instruc(CPU.getIr());
		ProcessSchedule.getRunningProcess().setTime_slice(0);   //�����жϽ���ʱ��Ƭ��Ϊ0
		ProcessSchedule.cpuStateProtection();  //cpu�ֳ�����
		CPU.switchToKernelState();  //�����жϴ���ʱCPUΪ�ں�̬
		
		//ȱҳ��ʱ�������������3
		if(ProcessSchedule.getRunningProcess().isLackPage()) {
			if(readDisk_interrupt==null) {
				readDisk_interrupt = CPU.getIr();
				readDisk_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //���ж�
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
			
			ProcessSchedule.setRunningProcess(null);  //ִ�н�����Ϊ��
			//CPU.switchToUserState();   //�ж��������CPU����Ϊ�û�̬
			CPU.setIr(null);
			CPU.setIs_busy(false);
			return;
		}
		
		if(CPU.getIr().getInstruc_state()==2) {
			if(keyboard_interrupt==null) {
				keyboard_interrupt = CPU.getIr();
				keyboard_time = IOInterrupt.getClock().getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==3) {
			if(screen_interrupt==null) {
				screen_interrupt = CPU.getIr();
				screen_time = IOInterrupt.getClock().getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==4) {
			if(readDisk_interrupt==null) {
				readDisk_interrupt = CPU.getIr();
				readDisk_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //���ж�
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==5) {
			if(writeDisk_interrupt==null) {
				writeDisk_interrupt = CPU.getIr();
				writeDisk_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //���ж�
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==6) {
			if(print_interrupt==null) {
				print_interrupt = CPU.getIr();
				print_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //���ж�
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		
		ProcessSchedule.setRunningProcess(null);  //ִ�н�����Ϊ��
		//CPU.switchToUserState();   //�ж��������CPU����Ϊ�û�̬
		CPU.setIr(null);
		CPU.setIs_busy(false);
	}
	public synchronized static void checkIOInterrupt() {  //�����������ж��Ƿ���ɣ����ҽ������������н����жϴ���
		if(ProcessSchedule.getBlockQ1().size()>0) {
			int time = IOInterrupt.getClock().getTime();
			if(time - keyboard_time>=2) {
				keyboard_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.getBlockQ1().get(0).getPcb().setInterrupt_instruc(null);  //�����������е��ж�ָ���ÿ�
				//�����ж��豸Ϊ��
				keyboard_interrupt = null;
				keyboard_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.getBlockQ1().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ1().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ1();
					ProcessSchedule.cancelProcess(p);
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(2,time);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.getBlockQ1().size()>0) {  //������Ҫ�жϵ��õ�ָ��
					keyboard_interrupt = ProcessSchedule.getBlockQ1().get(0).getPcb().getInterrupt_instruc();
					keyboard_time = time;
				}	
			}
		}
		if(ProcessSchedule.getBlockQ2().size()>0) {
			int time = IOInterrupt.getClock().getTime();
			if(time - screen_time>=1) {
				screen_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.getBlockQ2().get(0).getPcb().setInterrupt_instruc(null);  //��������ָ��Ϊ��
				screen_interrupt = null;
				screen_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.getBlockQ2().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ2().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ2();
					ProcessSchedule.cancelProcess(p);
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(3,time);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.getBlockQ2().size()>0) {  //������Ҫ�жϵ��õ�ָ��
					screen_interrupt = ProcessSchedule.getBlockQ2().get(0).getPcb().getInterrupt_instruc();
					screen_time = time;
				}
			}
		}
		
		
		if(ProcessSchedule.getBlockQ5().size()>0) {
			int time = IOInterrupt.getClock().getTime();
			if(time - print_time>=4) {
				print_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.getBlockQ5().get(0).getPcb().setInterrupt_instruc(null);  //��������ָ��Ϊ��
				print_interrupt = null;
				print_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.getBlockQ5().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ5().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ5();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //ȡ�����ж�
					
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(6,time);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.getBlockQ5().size()>0) {  //������Ҫ�жϵ��õ�ָ��
					print_interrupt = ProcessSchedule.getBlockQ5().get(0).getPcb().getInterrupt_instruc();
					print_time = time;
				} else {
					CPU.setIs_break_close(false);  //ȡ�����ж�
				}
			}
		}
		if(ProcessSchedule.getBlockQ1().size()==0 
				&& ProcessSchedule.getBlockQ2().size()==0
				&& ProcessSchedule.getBlockQ3().size()==0
				&& ProcessSchedule.getBlockQ4().size()==0
				&& ProcessSchedule.getBlockQ5().size()==0) {
			CPU.setPsw(1);  //���жϴ�����ת��CPUΪ�û�̬
		}
	}
	
	public synchronized static void checkDiskRWInterrupt() {  //�������ļ���д�ж��Ƿ���ɣ����ҽ������������н����жϴ���
		if(ProcessSchedule.getBlockQ3().size()>0) {
			int time = DiskRWInterrupt.getClock().getTime();
			if(time-readDisk_time>=3) {
				if(!ProcessSchedule.getBlockQ3().get(0).isLackPage()) {
					readDisk_interrupt.setFinished(true);  //��ָ��ִ�����
				} else {
					//����ȱҳ
					ProcessSchedule.getBlockQ3().get(0).setLackPage(false);
					ProcessSchedule.getBlockQ3().get(0).setFinishLRUTag(false);
				}
				ProcessSchedule.getBlockQ3().get(0).getPcb().setInterrupt_instruc(null);  //��������ָ��Ϊ��
				readDisk_interrupt = null;
				readDisk_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��,���Ҹ�ָ���Ѿ����
				if(ProcessSchedule.getBlockQ3().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ3().get(0).getPcb().getInstrucNum()
					&& ProcessSchedule.getBlockQ3().get(0).getPcb().getInstruc().isFinished()) {
					Process p = ProcessSchedule.popBlockQ3();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //ȡ�����ж�
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(4,time);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.getBlockQ3().size()>0) {  //������Ҫ�жϵ��õ�ָ��
					readDisk_interrupt = ProcessSchedule.getBlockQ3().get(0).getPcb().getInterrupt_instruc();
					readDisk_time = time;
				} else {
					CPU.setIs_break_close(false);  //ȡ�����ж�
				}
			}
		}
		
		if(ProcessSchedule.getBlockQ4().size()>0) {
			int time = DiskRWInterrupt.getClock().getTime();
			if(time-writeDisk_time>=4) {
				writeDisk_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.getBlockQ4().get(0).getPcb().setInterrupt_instruc(null);  //��������ָ��Ϊ��
				writeDisk_interrupt = null;
				writeDisk_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.getBlockQ4().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ4().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ4();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //ȡ�����ж�
					
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(5,time);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.getBlockQ4().size()>0) {  //������Ҫ�жϵ��õ�ָ��
					writeDisk_interrupt = ProcessSchedule.getBlockQ4().get(0).getPcb().getInterrupt_instruc();
					writeDisk_time = time;
				} else {
					CPU.setIs_break_close(false);  //ȡ�����ж�
				}
			}
		}
		
		if(ProcessSchedule.getBlockQ1().size()==0 
				&& ProcessSchedule.getBlockQ2().size()==0
				&& ProcessSchedule.getBlockQ3().size()==0
				&& ProcessSchedule.getBlockQ4().size()==0
				&& ProcessSchedule.getBlockQ5().size()==0) {
			CPU.setPsw(1);  //���жϴ�����ת��CPUΪ�û�̬
		}
	}
}
