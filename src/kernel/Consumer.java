package kernel;

import hardware.RAM;

/**
 * 用于缓冲区读取的生产者消费者问题：消费者
 */


public class Consumer {
	
	PV empty;   //可用空闲缓冲区个数
	PV full;   //缓冲区满的个数
	PV mutex;   //临界区互斥信号量
	

	public Consumer(PV e,PV f,PV m) {
		empty = e;
		full = f;
		mutex = m;
	}
	public void takeData(Process p) {
		full.P();       //读取一个满缓冲区
		mutex.P();      //进入临界区
		
		//从内存缓冲区上取走数据
		//System.out.println("从内存缓冲区取走数据----");
		RAM.getAllBlocks()[p.getBufferNo()] = false;
		p.setBufferNo(-1);
		
		mutex.V();      //离开临界区
		empty.V();      //缓冲区变空
	}
	
	
}
