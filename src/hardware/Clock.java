package hardware;


public class Clock {  //ʱ����
	private int time;   //ʱ�Ӿ���ʱ��
	public Clock() {
		this.time = 0;
	}
	public int getTime() {
		return time;
	}
	public void passOneSec() {
		time++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println("Time is " + time);
		//ProcessUI.getTimeText().setText(String.valueOf(time));
	}
}