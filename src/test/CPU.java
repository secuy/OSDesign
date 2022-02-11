package test;

public class CPU {
	private static int pc = 0;   //程序计数器，下一条将执行的指令编号
	private static Instruction ir = null;   //指令寄存器，正在执行的指令信息
	private static int psw = 1;  //状态寄存器,0表示内核态，1表示用户态
	private static boolean is_busy = false;  //cpu当前是否在执行指令
	private static boolean is_break_close = false;  //是否是关中断状态
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
			ProcessUI.setCPUbreakMessage("关中断");
		} else {
			ProcessUI.setCPUbreakMessage("开中断");
		}
	}
	public static void excute() {  //执行当前CPU指令
		//经过1s的执行完成一条指令
		ProcessUI.OutputMessage();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clock.passOneSec();
		if(ir.getInstruc_state()==0) {  //0状态指令直接执行完毕，其他类型指令必须在中断结束后才算结束
			ir.setFinished(true);  //该指令执行完毕
		}
	}
	public static void nextInstruction(Process p) {   //切换同一进程中下一条指令
		if(p.getPcb().getPc()<=p.getPcb().getInstrucNum()) {
			ir = p.getInstructions().get(pc-1);
			p.getPcb().setInstruc(p.getInstructions().get(pc-1));
			p.getPcb().setPc(pc+1);
			pc++;
		}
	}
}
