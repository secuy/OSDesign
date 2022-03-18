package kernel;

import other.OSManage;
import hardware.Clock;

/**
 * 
 * ��������жϴ���
 *
 */


public class IOInterrupt extends Thread {
	private static Clock clock;
	
	OSManage om;
	
	public IOInterrupt(OSManage om) {
		clock = new Clock();
		this.om = om;
	}
	
	public static Clock getClock() {
		return clock;
	}
	
	
	public void run() {
		while(!om.isShutdown()) {
			if(!om.isPause()) {
				//����ж�,��������ɵ��ж�
				InterruptOperator.checkIOInterrupt();
				
				clock.passOneSec();
			}
			
		}
	}
	
}
