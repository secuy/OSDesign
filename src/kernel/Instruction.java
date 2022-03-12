package kernel;

public class Instruction {  //指令类
	
	private int instruc_ID;   //指令编号（作业中指令编号，从1开始）
	private int instruc_state;   //指令类型标志
	private int L_Address;   //用户程序指令访问的逻辑地址
	private int inRunTimes;   //每条指令运行的时间
	private int finishRunTimes;  //该条指令执行的时间
	private boolean isFinished;   //是否执行完成
	
	
	
	public Instruction() {  //初始化，还未表示指令
		instruc_ID = 0;
		instruc_state = -1;
		isFinished = false;
		
	}
	public Instruction(int instrucID, int instrucState, int L_Address, int inRunTimes) {
		instruc_ID = instrucID;
		instruc_state = instrucState;
		this.L_Address = L_Address;
		this.inRunTimes = inRunTimes;
		this.finishRunTimes = 0;
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
	public int getL_Address() {
		return L_Address;
	}
	public void setL_Address(int lAddress) {
		L_Address = lAddress;
	}
	public int getInRunTimes() {
		return inRunTimes;
	}
	public void setInRunTimes(int inRunTimes) {
		this.inRunTimes = inRunTimes;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public int getFinishRunTimes() {
		return finishRunTimes;
	}
	public void setFinishRunTimes(int finishRunTimes) {
		this.finishRunTimes = finishRunTimes;
	}
	public void addFinishRunTimes() {
		finishRunTimes++;
	}
	
	public void finishInstruc() {
		this.isFinished = true;
	}
	
	
	@Override
	public String toString() {
		return "Instruction [instruc_ID=" + instruc_ID + ", instruc_state="
				+ instruc_state + ", L_Address=" + L_Address + ", inRunTimes="
				+ inRunTimes + ", isFinished=" + isFinished + "]";
	}
	
}
