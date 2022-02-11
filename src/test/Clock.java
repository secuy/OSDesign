package test;

public class Clock {  //时钟类
	private static int time = 0;   //时钟经过时间
	public Clock() {}
	public int getTime() {
		return time;
	}
	public void passOneSec() {
		time++;
		ProcessUI.texts[0].setText(String.valueOf(time));
	}
}