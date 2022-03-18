package kernel;

import other.OSManage;
import hardware.Clock;

/**
 * 
 * 输入输出中断处理
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
				//检查中断,并处理完成的中断
				InterruptOperator.checkIOInterrupt();
				
				clock.passOneSec();
			}
			
		}
	}
	
}
