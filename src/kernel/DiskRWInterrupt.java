package kernel;

import hardware.Clock;

/**
 * ���̶�д�жϴ����߳���
 * 
 */


public class DiskRWInterrupt extends Thread{
	private static Clock clock;
	
	public DiskRWInterrupt() {
		clock = new Clock();
	}
	public static Clock getClock() {
		return clock;
	}
	
	public void run() {
		while(true) {
			
			//����ж�,��������ɵ��ж�
			InterruptOperator.checkDiskRWInterrupt();
			
			clock.passOneSec();
			
		}
	}
}
