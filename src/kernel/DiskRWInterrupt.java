package kernel;

import other.OSManage;
import hardware.Clock;

/**
 * ���̶�д�жϴ����߳���
 * 
 */


public class DiskRWInterrupt extends Thread{
	private static Clock clock;
	
	OSManage om;
	
	public DiskRWInterrupt(OSManage om) {
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
				InterruptOperator.checkDiskRWInterrupt();
				
				clock.passOneSec();
			}
			
		}
	}
}
