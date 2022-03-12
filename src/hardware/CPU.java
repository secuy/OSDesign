package hardware;

import kernel.Instruction;
import kernel.Process;
import kernel.ProcessSchedule;
import ui.ProcessUI;

public class CPU {
	
	private static final int KERNEL_STATE = 0;
	private static final int USER_STATE = 1;
	
	
	private static int pc = 0;   //�������������һ����ִ�е�ָ����
	private static Instruction ir = null;   //ָ��Ĵ���������ִ�е�ָ����Ϣ
	private static int psw = 1;  //״̬�Ĵ���,0��ʾ�ں�̬��1��ʾ�û�̬
	private static boolean is_busy = false;  //cpu��ǰ�Ƿ���ִ��ָ��
	private static boolean is_break_close = false;  //�Ƿ��ǹ��ж�״̬
	public CPU() {
		
	}
	public static int getPc() {
		return pc;
	}
	public static void setPc(int pc) {
		CPU.pc = pc;
	}
	public static Instruction getIr() {
		return ir;
	}
	public static void setIr(Instruction ir) {
		CPU.ir = ir;
	}
	public static int getPsw() {
		return psw;
	}
	public static void setPsw(int psw) {
		CPU.psw = psw;
		//ProcessUI.setCPUpswMessage(psw);
	}
	public static boolean isIs_busy() {
		return is_busy;
	}
	public static void setIs_busy(boolean isBusy) {
		is_busy = isBusy;
	}
	
	public static boolean isIs_break_close() {
		return is_break_close;
	}
	public static void setIs_break_close(boolean isBreakClose) {
		is_break_close = isBreakClose;
		/*if(isBreakClose) {
			ProcessUI.setCPUbreakMessage("���ж�");
		} else {
			ProcessUI.setCPUbreakMessage("���ж�");
		}*/
	}
	
	//�л����û�̬
	public static void switchToUserState() {
		psw = USER_STATE;
	}
	
	//�л����ں�̬
	public static void switchToKernelState() {
		psw = KERNEL_STATE;
	}
	
	public synchronized static void excute() {  //ִ�е�ǰCPUָ��
		//���ӵ�ǰָ������ʱ��
		ir.addFinishRunTimes();
		//ָ��ִ����ɺ�������ɱ�־
		if(ir.getFinishRunTimes()>=ir.getInRunTimes()) {
			ir.setFinished(true);  //��ָ��ִ�����
		}
		
		System.out.println("����ִ��ָ�"+ir.toString());
		/*if(ir.getInstruc_state()==0) {  //0״ָ̬��ֱ��ִ����ϣ���������ָ��������жϽ�����������
			ir.setFinished(true);  //��ָ��ִ�����
		}*/
	}
	public synchronized static void nextInstruction(Process p) {   //�л�ͬһ��������һ��ָ��
		//���ָ����������һ��ָ��
		if(ir!=null && !ir.isFinished()) {
			return;
		}
		if(p.getPcb().getPc()<=p.getPcb().getInstrucNum()) {
			ir = p.getInstructions().get(pc-1);
			p.getPcb().setInstruc(p.getInstructions().get(pc-1));
			p.getPcb().setPc(pc+1);
			pc++;
		}
	}
}
