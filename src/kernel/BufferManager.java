package kernel;

import hardware.RAM;

/**
 * 缓冲区管理
 */

public class BufferManager {
	
	private PV empty;
	private PV full;
	private PV mutex;
	
	private Producer p;
	private Consumer c;
	
	public BufferManager() {
		empty = new PV(RAM.BUFFER_AREA_NUM);    //空缓冲区最开始有个
		full = new PV(0);   //满缓冲区最开始没有
		mutex = new PV(1);
		
		
		p = new Producer(empty,full,mutex);
		c = new Consumer(empty,full,mutex);
	}

	public Producer getP() {
		return p;
	}

	public Consumer getC() {
		return c;
	}
	
}
