package kernel;

import hardware.Clock;

/**
 * 
 * ��������жϴ���
 *
 */


public class IOInterrupt extends Thread {
	private static Clock clock;
	
	public IOInterrupt() {
		clock = new Clock();
	}
	
	public static Clock getClock() {
		return clock;
	}
	
	
	public void run() {
		while(true) {
			
			//����ж�,��������ɵ��ж�
			InterruptOperator.checkIOInterrupt();
			
			clock.passOneSec();
			
		}
	}
	
}
