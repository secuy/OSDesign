package kernel;

import hardware.Clock;

/**
 * 磁盘读写中断处理线程类
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
			
			//检查中断,并处理完成的中断
			InterruptOperator.checkDiskRWInterrupt();
			
			clock.passOneSec();
			
		}
	}
}
