package test;

public class Instruction {  //指令类
	
	private int instruc_ID;   //指令编号（作业中指令编号，从1开始）
	private int instruc_state;   //指令类型标志
	 /*0表示进程正常调度，党时间片到，进程切换
	   1发生系统调用，CPU进行模式切换，运行进程进入阻塞态，进入键盘输入模块
	   2发生系统调用，CPU进行模式切换，运行进程进入阻塞态，进入屏幕显示模块
	   3发生进程调度，进程进入阻塞态，关中断*/
	private boolean isFinished;   //是否执行完成
	
	public Instruction() {  //初始化，还未表示指令
		instruc_ID = 0;
		instruc_state = -1;
		isFinished = false;
	}
	public Instruction(int instrucID, int instrucState) {
		instruc_ID = instrucID;
		instruc_state = instrucState;
		isFinished = false;
	}
	public int getInstruc_ID() {
		return instruc_ID;
	}
	public void setInstruc_ID(int instrucID) {
		instruc_ID = instrucID;
	}
	public int getInstruc_state() {
		return instruc_state;
	}
	public void setInstruc_state(int instrucState) {
		instruc_state = instrucState;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
