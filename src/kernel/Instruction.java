package kernel;

public class Instruction {  //ָ����
	
	private int instruc_ID;   //ָ���ţ���ҵ��ָ���ţ���1��ʼ��
	private int instruc_state;   //ָ�����ͱ�־
	private int L_Address;   //�û�����ָ����ʵ��߼���ַ
	private int inRunTimes;   //ÿ��ָ�����е�ʱ��
	private int finishRunTimes;  //����ָ��ִ�е�ʱ��
	private boolean isFinished;   //�Ƿ�ִ�����
	
	
	
	public Instruction() {  //��ʼ������δ��ʾָ��
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
