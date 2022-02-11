package test;

public class Instruction {  //ָ����
	
	private int instruc_ID;   //ָ���ţ���ҵ��ָ���ţ���1��ʼ��
	private int instruc_state;   //ָ�����ͱ�־
	 /*0��ʾ�����������ȣ���ʱ��Ƭ���������л�
	   1����ϵͳ���ã�CPU����ģʽ�л������н��̽�������̬�������������ģ��
	   2����ϵͳ���ã�CPU����ģʽ�л������н��̽�������̬��������Ļ��ʾģ��
	   3�������̵��ȣ����̽�������̬�����ж�*/
	private boolean isFinished;   //�Ƿ�ִ�����
	
	public Instruction() {  //��ʼ������δ��ʾָ��
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
