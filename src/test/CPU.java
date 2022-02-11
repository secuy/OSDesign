package test;

public class CPU {
	private static int pc = 0;   //�������������һ����ִ�е�ָ����
	private static Instruction ir = null;   //ָ��Ĵ���������ִ�е�ָ����Ϣ
	private static int psw = 1;  //״̬�Ĵ���,0��ʾ�ں�̬��1��ʾ�û�̬
	private static boolean is_busy = false;  //cpu��ǰ�Ƿ���ִ��ָ��
	private static boolean is_break_close = false;  //�Ƿ��ǹ��ж�״̬
	static Clock clock = new Clock();
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
		ProcessUI.setCPUpswMessage(psw);
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
		if(isBreakClose) {
			ProcessUI.setCPUbreakMessage("���ж�");
		} else {
			ProcessUI.setCPUbreakMessage("���ж�");
		}
	}
	public static void excute() {  //ִ�е�ǰCPUָ��
		//����1s��ִ�����һ��ָ��
		ProcessUI.OutputMessage();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clock.passOneSec();
		if(ir.getInstruc_state()==0) {  //0״ָ̬��ֱ��ִ����ϣ���������ָ��������жϽ�����������
			ir.setFinished(true);  //��ָ��ִ�����
		}
	}
	public static void nextInstruction(Process p) {   //�л�ͬһ��������һ��ָ��
		if(p.getPcb().getPc()<=p.getPcb().getInstrucNum()) {
			ir = p.getInstructions().get(pc-1);
			p.getPcb().setInstruc(p.getInstructions().get(pc-1));
			p.getPcb().setPc(pc+1);
			pc++;
		}
	}
}