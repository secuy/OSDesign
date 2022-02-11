package test;

public class InterruptOperator {   //�жϴ���
	private static Instruction keyboard_interrupt = null;  //�����ж�ָ���Ӧָ������Ϊ1��Ϊ��˵������
	private static int keyboard_time = -1;  //��������жϴ����ʱ�䣨ע���������������ʱ�䲻ͬ��
	private static Instruction screen_interrupt = null;  //��Ļ�жϣ���Ӧָ������Ϊ2
	private static int screen_time = -1;  //������Ļ�жϴ����ʱ��
	private static Instruction PV_interrupt = null;  //PV�����жϣ���Ӧָ������Ϊ3
	private static int PV_time = -1;  //����PV�жϴ����ʱ��
	
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
	public static int getPV_time() {
		return PV_time;
	}
	public static void setPV_time(int pVTime) {
		PV_time = pVTime;
	}
	public static Instruction getScreen() {
		return screen_interrupt;
	}
	public static void inInterrupt() {  //�����жϴ���
		if(CPU.getIr().getInstruc_state()==0) {
			return;
		}
		ProcessSchedule.getRunningProcess().getPcb().setInterrupt_instruc(CPU.getIr());
		ProcessSchedule.getRunningProcess().setTime_slice(0);   //�����жϽ���ʱ��Ƭ��Ϊ0
		ProcessSchedule.cpuStateProtection();  //cpu�ֳ�����
		CPU.setPsw(0);  //�����жϴ���ʱCPUΪ�ں�̬
		if(CPU.getIr().getInstruc_state()==1) {
			if(keyboard_interrupt==null) {
				keyboard_interrupt = CPU.getIr();
				keyboard_time = CPU.clock.getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==2) {
			if(screen_interrupt==null) {
				screen_interrupt = CPU.getIr();
				screen_time = CPU.clock.getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==3) {
			if(PV_interrupt==null) {
				PV_interrupt = CPU.getIr();
				PV_time = CPU.clock.getTime();
			}
			CPU.setIs_break_close(true);  //���ж�
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		ProcessSchedule.setRunningProcess(null);  //ִ�н�����Ϊ��
		CPU.setIr(null);
		CPU.setIs_busy(false);
	}
	public static void checkInterruptOperator() {  //����ж��Ƿ���ɣ����ҽ������������н����жϴ���
		if(ProcessSchedule.BlockQ1.size()>0) {
			if(CPU.clock.getTime()-keyboard_time>=4) {
				keyboard_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.BlockQ1.get(0).getPcb().setInterrupt_instruc(null);  //�����������е��ж�ָ���ÿ�
				//�����ж��豸Ϊ��
				keyboard_interrupt = null;
				keyboard_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.BlockQ1.get(0).getPcb().getPc()>ProcessSchedule.BlockQ1.get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ1();
					ProcessSchedule.cancelProcess(p);
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(1);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.BlockQ1.size()>0) {  //������Ҫ�жϵ��õ�ָ��
					keyboard_interrupt = ProcessSchedule.BlockQ1.get(0).getPcb().getInterrupt_instruc();
					keyboard_time = CPU.clock.getTime();
				}	
			}
		}
		if(ProcessSchedule.BlockQ2.size()>0) {
			if(CPU.clock.getTime()-screen_time>=3) {
				screen_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.BlockQ2.get(0).getPcb().setInterrupt_instruc(null);  //��������ָ��Ϊ��
				screen_interrupt = null;
				screen_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.BlockQ2.get(0).getPcb().getPc()>ProcessSchedule.BlockQ2.get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ2();
					ProcessSchedule.cancelProcess(p);
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(2);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.BlockQ2.size()>0) {  //������Ҫ�жϵ��õ�ָ��
					screen_interrupt = ProcessSchedule.BlockQ2.get(0).getPcb().getInterrupt_instruc();
					screen_time = CPU.clock.getTime();
				}
			}
		}
		if(ProcessSchedule.BlockQ3.size()>0) {
			if(CPU.clock.getTime()-PV_time>=2) {
				PV_interrupt.setFinished(true);  //��ָ��ִ�����
				ProcessSchedule.BlockQ3.get(0).getPcb().setInterrupt_instruc(null);  //��������ָ��Ϊ��
				PV_interrupt = null;
				PV_time = -1;
				//�ж��Ƿ�Ϊ���һ��ָ��
				if(ProcessSchedule.BlockQ3.get(0).getPcb().getPc()>ProcessSchedule.BlockQ3.get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ3();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //ȡ�����ж�
					
				} else {
					//�����������ж�ͷ����
					ProcessSchedule.awakeProcess(3);
				}
				//�жϷ���Ϊ���������е���һ�����̷���
				if(ProcessSchedule.BlockQ3.size()>0) {  //������Ҫ�жϵ��õ�ָ��
					PV_interrupt = ProcessSchedule.BlockQ3.get(0).getPcb().getInterrupt_instruc();
					PV_time = CPU.clock.getTime();
				} else {
					CPU.setIs_break_close(false);  //ȡ�����ж�
				}
			}
		}
		if(ProcessSchedule.BlockQ1.size()==0 
				&& ProcessSchedule.BlockQ2.size()==0
				&& ProcessSchedule.BlockQ3.size()==0) {
			CPU.setPsw(1);  //���жϴ�����ת��CPUΪ�û�̬
		}
	}
}
