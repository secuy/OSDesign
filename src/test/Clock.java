package test;

public class Clock {  //ʱ����
	private static int time = 0;   //ʱ�Ӿ���ʱ��
	public Clock() {}
	public int getTime() {
		return time;
	}
	public void passOneSec() {
		time++;
		ProcessUI.texts[0].setText(String.valueOf(time));
	}
}