package kernel;

import hardware.Clock;

/**
 * 
 * 输入输出中断处理
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
			
			//检查中断,并处理完成的中断
			InterruptOperator.checkIOInterrupt();
			
			clock.passOneSec();
			
		}
	}
	
}
