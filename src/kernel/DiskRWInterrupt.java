package kernel;

import other.OSManage;
import hardware.Clock;

/**
 * 磁盘读写中断处理线程类
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
				//检查中断,并处理完成的中断
				InterruptOperator.checkDiskRWInterrupt();
				
				clock.passOneSec();
			}
			
		}
	}
}
